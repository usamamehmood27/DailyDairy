package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentDashboardBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.splashcallback
import kamai.app.ads.AdmobManager
import kamai.app.ads.AdsManager


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        splashcallback.onClick()
        AdmobManager.loadBannerAd1(requireActivity(),binding.bannerAd)

        util.logAnalytic(requireContext(), "Dashboard_Landed")
        binding.menu.setOnClickListener {
            util.logAnalytic(requireContext(), "SBM_click")
            controller.navigate(R.id.action_dashboardFragment_to_sildeMenuFragment)
        }
        binding.creatdairy.setOnClickListener {
            util.logAnalytic(requireContext(), "WRD_click")
            AdsManager.showIntersWithClick(requireActivity()) {
                checkingState = "CreateNote"
                controller.navigate(R.id.action_dashboardFragment_to_dairyPageFragment)
            }
        }
        binding.save.setOnClickListener {
            util.logAnalytic(requireContext(), "WSN_clicked")
            AdsManager.showIntersWithClick(requireActivity()) {
                controller.navigate(R.id.action_dashboardFragment_to_saveNotes_Fragment)
            }

        }
        binding.folders.setOnClickListener {
            util.logAnalytic(requireContext(), "folder_clicked")
            AdsManager.showIntersWithClick(requireActivity()) {
                controller.navigate(R.id.action_dashboardFragment_to_folders_Fragment)
            }

        }
        binding.draft.setOnClickListener {
            util.logAnalytic(requireContext(), "draft_clicked")
            AdsManager.showIntersWithClick(requireActivity()) {
                controller.navigate(R.id.action_dashboardFragment_to_draft_Fragment)
            }

        }

        return binding.root
    }

}