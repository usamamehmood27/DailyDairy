package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentSettingBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.CheckSharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.cancelAlarm
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.clearCache
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.setDailyAlarm
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showTimePickerDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        util.logAnalytic(requireContext(), "setting_land")
        binding.apply {
            CheckSharedPref(requireActivity(),binding)
            lockselect.setOnClickListener {
                util.logAnalytic(requireContext(), "set_dairy_lock")
                    SharedPref.putBolean("resetpin",false)
                    if (!SharedPref.getBolean("lock", false)) {
                        lockselect.setImageResource(R.drawable.unselect)
                        SharedPref.putBolean("lock", true)
                        controller.navigate(R.id.action_settingFragment_to_lockFragment)
                    } else if (SharedPref.getBolean("lock", false)){
                        SharedPref.putBolean("startscreen",false)
                        lockselect.setImageResource(R.drawable.sellect)
                        SharedPref.putBolean("lock", false)
                    }

            }
//            clocksellect.setOnClickListener {
//                if (!SharedPref.getBolean("setalaram", false)) {
//                    showTimePickerDialog(requireActivity(), object : TimePickerDialog.OnTimeSetListener {
//                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//                            SharedPref.putBolean("setalaram", true)
//                            clocksellect.setImageResource(R.drawable.unselect)
//                            setDailyAlarm(requireActivity(), hourOfDay, minute)
//                        }
//                    })
//                } else if (SharedPref.getBolean("setalaram", false)){
//                    SharedPref.putBolean("setalaram", false)
//                    clocksellect.setImageResource(R.drawable.sellect)
//                    cancelAlarm(requireActivity())
//                }
//            }
            clearsellect.setOnClickListener {
                util.logAnalytic(requireContext(), "clear_app_data")
                if (!SharedPref.getBolean("cleardata", false)) {
                    clearsellect.setImageResource(R.drawable.unselect)
                    SharedPref.putBolean("cleardata", true)
                    clearCache(requireActivity())

                } else if (SharedPref.getBolean("cleardata", false)){
                    clearsellect.setImageResource(R.drawable.sellect)
                    SharedPref.putBolean("cleardata", false)
                }
            }
            skipsellect.setOnClickListener {
                util.logAnalytic(requireContext(), "skip_mood_selection")
                if (!SharedPref.getBolean("skipmood", false)) {
                    skipsellect.setImageResource(R.drawable.unselect)
                    SharedPref.putBolean("skipmood", true)
                    showToast(requireActivity(), "Mood sellection will not be shown")
                } else if (SharedPref.getBolean("skipmood", false)){
                    skipsellect.setImageResource(R.drawable.sellect)
                    SharedPref.putBolean("skipmood", false)
                }
            }
            backpress.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

    }
}