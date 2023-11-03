package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentFoldersBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.FolderAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.createFolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.renamefolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.currentfoldername
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel


class Folders_Fragment : Fragment() {
    lateinit var binding: FragmentFoldersBinding
    lateinit var viemodel: Notes_ViewModel
    lateinit var folderlist: MutableList<Any>

    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoldersBinding.inflate(layoutInflater)
        viemodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]
        util.logAnalytic(requireContext(), "folder_landed")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backpress.setOnClickListener {
                requireActivity().onBackPressed()
            }

            setRecycle()
            createmore.setOnClickListener {
                util.logAnalytic(requireContext(), "create_folder")
                createFolder(requireContext(), viemodel) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        setRecycle()
                    }, 50)

                }
            }
        }
    }

    fun setRecycle() {
        binding.apply {
            viemodel.getfolders()
            viemodel.listoffolder.observe(viewLifecycleOwner) {
                folderlist = mutableListOf()
                if (!it.isNullOrEmpty())
                    folderlist.addAll(it)
                if (folderlist.size >= 1)
                    nofolder.visibility = View.GONE
                else
                    nofolder.visibility = View.VISIBLE

                for (i in folderlist.indices) {
                    if (i == 2) {
                        folderlist.add(i, "")
                    }
                }
                folderrecycle.layoutManager = LinearLayoutManager(requireContext())
                folderrecycle.adapter =
                    FolderAdapter(requireActivity(), viemodel, folderlist) { file, folder ->
                        if (file == "rename") {
                            util.logAnalytic(requireContext(), "folder_rename")
                            renamefolder(requireActivity(), viemodel, folder) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    setRecycle()
                                }, 50)
                            }
                        } else {
                            currentfoldername = folder.name
                            viemodel.searchNotesByFolder(currentfoldername)
                            controller.navigate(R.id.action_folders_Fragment_to_folder_Note_Fragment)
                        }

                    }
            }
        }

    }

}