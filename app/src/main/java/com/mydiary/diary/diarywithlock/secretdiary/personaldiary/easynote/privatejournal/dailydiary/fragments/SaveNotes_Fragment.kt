package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentSaveNotesBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.NotesAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.SearchAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.allFolders
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.createFolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.deletedraft
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.currentfoldername
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel

class SaveNotes_Fragment : Fragment(){
    lateinit var binding: FragmentSaveNotesBinding
    lateinit var viemodel: Notes_ViewModel
    lateinit var adapter: NotesAdapter
    var fileAdapter: SearchAdapter? = null
    private var noteslist = mutableListOf<Any>()

    companion object {
        var checkingState = ""
        var checkz: Boolean=false
    }

    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        util.logAnalytic(requireContext(), "YSN_landed")
        binding = FragmentSaveNotesBinding.inflate(layoutInflater)
        viemodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viemodel.getNotes()
        viemodel.getfolders()
        binding.apply {
            saverecycle.layoutManager = LinearLayoutManager(requireContext())
            viemodel.listofnotes.observe(viewLifecycleOwner) {
                noteslist.clear()
                for (i in it){
                    i as Notes
                    if (i.folderId=="")
                        noteslist.add(i)

                }

                if (noteslist.size >= 1)
                    nofolder.visibility = View.GONE
                else
                    nofolder.visibility = View.VISIBLE
                noteslist.reverse()
                for (i in noteslist.indices) {
                    if (i == 2) {
                        noteslist.add(i, "")
                    }
                }
                setadapter(noteslist)
                binding.saverecycle.adapter = adapter
            }
            backpress.setOnClickListener {
                requireActivity().onBackPressed()
            }
//            reverse.setOnClickListener {
//                noteslist.reverse()
//                setadapter(noteslist)
//                binding.saverecycle.adapter = adapter
//            }
//            searchView.setOnQueryTextListener(object :
//                SearchView.OnQueryTextListener {
//
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    searchView.clearFocus()
//                    return false
//                }
//
//                @RequiresApi(Build.VERSION_CODES.O)
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    if (newText!!.isNotEmpty()) {
//                        searchItems(newText, noteslist)
//                        searchrecycle.visibility = View.VISIBLE
//                    } else {
//                        searchItems(newText, noteslist)
//                        searchrecycle.visibility = View.GONE
//                    }
//
//                    return false
//                }
//
//            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFilesAdapter(list: MutableList<Any>) {
        fileAdapter = SearchAdapter(requireActivity(), list) {
            checkingState = "SavedNotes"
            viemodel.onenote.postValue(it)
            controller.navigate(R.id.action_saveNotes_Fragment_to_dairyPageFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchItems(newText: String?, fileList: MutableList<Any>) {
        val query = newText!!.lowercase()
        val tempList = arrayListOf<Any>()
        if (query.isNotEmpty()) {
            fileList.forEach {
                val tempFile = it as Notes
                if (tempFile.content.contains(query))
                    tempList.add(tempFile)
            }
            setFilesAdapter(tempList)
            binding.saverecycle.visibility = View.INVISIBLE

        } else {
            setFilesAdapter(fileList)
            binding.saverecycle.visibility = View.VISIBLE

        }
    }

    fun setadapter(list: MutableList<Any>) {
        adapter =
            NotesAdapter(requireActivity(), list, viemodel) { notes, draftnotes, check, position ->
                if (check == "delete")
                    deletedraft(requireActivity(), notes, viemodel, position, list) {
                        util.logAnalytic(requireContext(), "YSN_delete")
                        adapter.notifyItemRemoved(it)
                        adapter.notifyItemRangeChanged(it, list.size)
                    }
                else if (check == "move") {
                    viemodel.listoffolder.observe(viewLifecycleOwner) { list->
                        allFolders(requireContext(), viemodel, list, notes) { name->
                            if (name != "") {
                                currentfoldername = name
                                showToast(requireActivity(), "File Moved Successfully")
                                util.logAnalytic(requireContext(), "YSN_move_folder")
                                controller.navigate(R.id.action_saveNotes_Fragment_to_folder_Note_Fragment)
                            } else {
                                    createFolder(requireContext(), viemodel) {
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            viemodel.getfolders()
                                        },100)
                                    }
                            }
                        }
                    }
                    } else {
                        checkingState = "SavedNotes"
                        viemodel.onenote.postValue(notes)
                        controller.navigate(R.id.action_saveNotes_Fragment_to_dairyPageFragment)
                    }
                }

            }

    }
