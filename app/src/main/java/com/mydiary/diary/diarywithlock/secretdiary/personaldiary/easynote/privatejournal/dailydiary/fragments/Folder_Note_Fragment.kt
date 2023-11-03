package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentFolderNoteBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.NotesAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.allFolders
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.createFolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.currentfoldername
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel


class Folder_Note_Fragment : Fragment() {
    lateinit var binding: FragmentFolderNoteBinding
    lateinit var viemodel: Notes_ViewModel
    lateinit var folderlist: MutableList<Any>
    lateinit var adapter: NotesAdapter
    var addedAtIndex2 = false

    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentFolderNoteBinding.inflate(layoutInflater)
        viemodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]
        binding.apply {
            viemodel.foldernaemlist.observe(viewLifecycleOwner){
                folderlist=it
                for (i in folderlist.indices) {
                    if (i == 2 && !addedAtIndex2) {
                        folderlist.add(i, "")
                        addedAtIndex2 = true
                    }
                }
                foldername.text= currentfoldername
                recycle.layoutManager=LinearLayoutManager(requireContext())

                setadapter(folderlist)
                recycle.adapter=adapter
            }
            backpress.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        return binding.root
    }
    fun setadapter(list: MutableList<Any>) {
        adapter =
            NotesAdapter(requireActivity(), list, viemodel) { notes, draftnotes, check, position ->
                if (check == "delete")
                    Dialog_Utils.deletedraft(
                        requireActivity(),
                        notes,
                        viemodel,
                        position,
                        list
                    ) {
                        adapter.notifyItemRemoved(it)
                        adapter.notifyItemRangeChanged(it, list.size)
                    }
                else if (check == "move") {
                    viemodel.getfolders()
                    viemodel.listoffolder.observe(viewLifecycleOwner) {
                        allFolders(requireContext(), viemodel, it, notes) {
                            Toast.makeText(requireActivity(), "folderAdded", Toast.LENGTH_SHORT)
                                .show()
                            if (it!="")
                            viemodel.searchNotesByFolder(it)
                            else{
                                createFolder(requireContext(), viemodel) {

                                }
                            }
                        }
                    }

                } else {
                    checkingState = "FolderNotes"
                    viemodel.onenote.postValue(notes)
                    controller.navigate(R.id.action_folder_Note_Fragment_to_dairyPageFragment)
                }
            }

    }

}