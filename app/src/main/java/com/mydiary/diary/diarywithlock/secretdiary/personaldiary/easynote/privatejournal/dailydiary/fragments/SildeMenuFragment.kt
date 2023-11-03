package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentSildeMenuBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.exitDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.rateusDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.showDialogPrivacyPOlicy
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import kamai.app.ads.AdmobManager
import kamai.app.ads.AdsManager

class SildeMenuFragment : Fragment() {
    lateinit var binding: FragmentSildeMenuBinding
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSildeMenuBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AdsManager.showNativeAd(requireActivity(),binding.nativead,"SildeMenuFragment")
        binding.apply {
            settings.setOnClickListener {
                util.logAnalytic(requireContext(), "setting_click")
                AdsManager.showIntersWithClick(requireActivity()){
                    controller.navigate(R.id.action_sildeMenuFragment_to_settingFragment)
                }
            }

            exit.setOnClickListener {
                util.logAnalytic(requireContext(), "exitapp_click")
                exitDialog(requireActivity())
            }
            rateus.setOnClickListener {
                util.logAnalytic(requireContext(), "rateus_click")
                rateusDialog(requireContext())
            }
            privacyPolicy.setOnClickListener {
                showDialogPrivacyPOlicy(requireContext())
            }
            language.setOnClickListener {
                util.logAnalytic(requireContext(), "Applanguages_click")
                AdsManager.showIntersWithClick(requireActivity()){
                    controller.navigate(R.id.action_sildeMenuFragment_to_languageFragment)

                }
            }
//            calender.setOnClickListener {
//                AdsManager.showIntersWithClick(requireActivity()){
//                    controller.navigate(R.id.action_sildeMenuFragment_to_clanderFragment)
//                }
//            }
            backpress.setOnClickListener {
                AdsManager.countInterstitialCapping(requireActivity())
                requireActivity().onBackPressed()
            }


        }
    }

}