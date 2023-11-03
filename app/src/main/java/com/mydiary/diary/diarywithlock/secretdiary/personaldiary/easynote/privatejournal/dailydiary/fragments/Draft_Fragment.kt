package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentDraftBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.NotesAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.deleteAlldraft
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.deletedraft
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel


class Draft_Fragment : Fragment() {
    lateinit var binding: FragmentDraftBinding
    lateinit var viemodel: Notes_ViewModel
    private var draftlist = mutableListOf<Any>()
    lateinit var adapter: NotesAdapter
    lateinit var item: Any
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDraftBinding.inflate(layoutInflater)
        viemodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]
        util.logAnalytic(requireContext(), "Draft_landed")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backpress.setOnClickListener {
                requireActivity().onBackPressed()
            }
            draftrecycle.layoutManager = LinearLayoutManager(requireContext())
            setRecycle()
            clear.setOnClickListener {
                deleteAlldraft(requireActivity(), viemodel) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        setRecycle()
                    }, 50)
                }
            }
        }
    }

    fun setRecycle() {
        binding.apply {
            viemodel.getDraftNotes()
            viemodel.listofdraft.observe(viewLifecycleOwner) {
                draftlist = it
                for (i in draftlist.indices) {
                    if (i == 2) {
                        draftlist.add(i, "")
                    }

                }
                if (draftlist.size >= 1) {
                    nofolder.visibility = View.GONE
                    clear.visibility = View.VISIBLE
                }
                adapter =
                    NotesAdapter(
                        requireActivity(),
                        it,
                        viemodel
                    ) { notes, draftnotes, check, position ->
                        if (check == "delete")
                            deletedraft(
                                requireActivity(),
                                draftnotes,
                                viemodel,
                                position, draftlist
                            ) {
                                adapter.notifyItemRemoved(it)
                                adapter.notifyItemRangeChanged(it, draftlist.size)
                            }
                        else {
                            checkingState = "DraftNotes"
                            viemodel.onenote.postValue(draftnotes)
                            controller.navigate(R.id.action_draft_Fragment_to_dairyPageFragment)
                        }

                    }
                binding.draftrecycle.adapter = adapter
            }
        }

    }

}