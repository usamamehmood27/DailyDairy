package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentSplashBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.FillAdData.Companion.remoteConfig
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.logAnalytic
import com.uxcam.UXCam
import com.uxcam.datamodel.UXConfig


class FragmentSplash : Fragment(){

    companion object {
        var key = ""
    }


    private lateinit var binding: FragmentSplashBinding
    private val splashHandler = Handler(Looper.getMainLooper())
    private val SPLASH_DELAY: Long = 8000



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        initializeAds()
        initializeUXCam()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSplash()
    }

    private fun loadSplash() {
//        setValue(requireActivity())
        logAnalytic(requireContext(), "Splash_view")

        SharedPref.putInt("numberOfStarts", SharedPref.getInt("numberOfStarts", 0) + 1)
        val stepInCount = SharedPref.getInt("numberOfStarts", 0).toString()
        logAnalytic(requireContext(), "${stepInCount}_Open")

        splashHandler.postDelayed({ showLayout() }, SPLASH_DELAY)


        binding.start.isEnabled = false
        binding.start.setOnClickListener {
            logAnalytic(requireContext(), "start_btn_click")
            findNavController().navigate(R.id.action_fragmentSplash_to_dashboardFragment)
        }
    }

    private fun initializeAds() {

        remoteConfig(requireContext())
    }

    private fun initializeUXCam() {
        val config = UXConfig.Builder(key)
            .enableAutomaticScreenNameTagging(true)
            .enableImprovedScreenCapture(true)
            .build()
        UXCam.startWithConfiguration(config)
    }



    private fun showLayout() {
        binding.LottieStart.visibility = View.GONE
        binding.text.text = getString(R.string.may_contain_ads)
        binding.start.visibility = View.VISIBLE
        binding.start.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        splashHandler.removeCallbacksAndMessages(null)
    }
}
