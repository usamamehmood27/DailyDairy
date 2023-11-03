package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentClanderBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.SearchAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
import kamai.app.ads.AdsManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ClanderFragment : Fragment() {
    lateinit var binding: FragmentClanderBinding
    lateinit var viewmodel: Notes_ViewModel
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClanderBinding.inflate(layoutInflater)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, day)
        val dateFormat =SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
        val dateString = dateFormat.format(selectedDate.time)
        val currentDate = Date()
        val dayName = getDayName(currentDate)
        viewmodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]
        binding.apply {
            viewmodel.searchNotesByDate(dateFormat)
            completedate.text = StringBuilder("$dayName,$dateString")
            backpress.setOnClickListener {
                AdsManager.showInterstitialWithClick(requireActivity()) {
                    requireActivity().onBackPressed()
                }
            }
            viewmodel.calanderlist.observe(viewLifecycleOwner) { list ->
                if (list.size >= 1)
                    binding.nodairy.visibility = View.GONE
                else
                    binding.nodairy.visibility = View.VISIBLE
                for (i in list.indices) {
                    if (i == 3) {
                        list.add(i, "")
                    }

                }
                calanderrecycle.adapter = SearchAdapter(requireActivity(), list) {
                    checkingState = "SavedNotes"
                    viewmodel.onenote.postValue(it)
                    controller.navigate(R.id.action_clanderFragment_to_dairyPageFragment)
                }
            }
            calanderrecycle.layoutManager = LinearLayoutManager(requireContext())
            showDatePickerDialog() {
                viewmodel.calanderlist.observe(viewLifecycleOwner) { listitem ->
                    if (listitem.size >= 1)
                        binding.nodairy.visibility = View.GONE
                    else
                        binding.nodairy.visibility = View.VISIBLE
                    for (i in listitem.indices) {
                        if (i == 3) {
                            listitem.add(i, "")
                        }
                    }
                    calanderrecycle.adapter = SearchAdapter(requireActivity(), listitem) {
                        checkingState = "SavedNotes"
                        viewmodel.onenote.postValue(it)
                        controller.navigate(R.id.action_clanderFragment_to_dairyPageFragment)
                    }
                }

            }
            plus.setOnClickListener {
//                showDatePickerDialog() {
//                    viewmodel.calanderlist.observe(viewLifecycleOwner) { list ->
//                        if (list.size >= 1)
//                            binding.nodairy.visibility = View.GONE
//                        else
//                            binding.nodairy.visibility = View.VISIBLE
//                        calanderrecycle.adapter = SearchAdapter(requireActivity(), list) {
//                            checkingState = "SavedNotes"
//                            viewmodel.onenote.postValue(it)
//                            controller.navigate(R.id.action_clanderFragment_to_dairyPageFragment)
//                        }
//                    }
//
//                }
            }

        }

        return binding.root
    }
    fun getDayName(date: Date): String {
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dateFormat.format(date)
    }
    private fun showDatePickerDialog(click: () -> Unit) {

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val dayName = dateFormat.format(selectedDate.time)

            val formattedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
            val message = "$dayName,$formattedDate"
            binding.completedate.text = message
            viewmodel.searchNotesByDate(formattedDate)
            click.invoke()
        }
//        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
//            // Do something with the selected date
//            val selectedDate = Calendar.getInstance()
//            selectedDate.set(year, month, dayOfMonth)
//
//
//
//        }, year, month, day)
//        datePickerDialog.show()
    }


}