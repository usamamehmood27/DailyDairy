package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.DailyDiary.databinding.ActivitySplashBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.FillAdData.Companion.remoteConfig
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.FillAdData.Companion.setValue
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.logAnalytic
import com.uxcam.UXCam
import com.uxcam.datamodel.UXConfig
import kamai.app.ads.AdsHelper
import kamai.app.ads.AdsManager
import kamai.app.billing.BillingManager
import kamai.app.enums.AdType
import kamai.app.interfaces.IAdListener
import kamai.app.interfaces.IEventListener

class SplashActivity : AppCompatActivity(), IAdListener, IEventListener {
    var stepin = ""

    companion object {
        var key = ""
    }

    val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setValue(this@SplashActivity)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        logAnalytic(this, "Event_splash_onCreate")
        logAnalytic(this, "Splash_view")
        SharedPref.putInt("numberOfStarts", SharedPref.getInt("numberOfStarts", 0) + 1)
        stepin = SharedPref.getInt("numberOfStarts", 0).toString()
        logAnalytic(this, "${stepin}_Open")

        AdsManager.startRequestingAds(application, this)
        AdsHelper.eventListener = this
//        AdsManager.initAdmob(this) {}
        remoteConfig(this@SplashActivity)

        val config = UXConfig.Builder(key)
            .enableAutomaticScreenNameTagging(true)
            .enableImprovedScreenCapture(true)
            .build()
        UXCam.startWithConfiguration(config);
        Handler(Looper.myLooper()!!).postDelayed({
            showLayout()
        }, 8000)
        binding.start.setOnClickListener {
            logAnalytic(this, "start_btn_click")
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }

    fun showLayout() {
        binding.LottieStart.visibility = View.GONE
        binding.text.text = "This action may contain Ad"
        binding.start.visibility = View.VISIBLE
    }

    override fun onAdResponse(ad: AdType) {
        if (ad == AdType.NATIVE_INFLATION) {
            logAnalytic(this, "Native_INFLATION")
            if (SharedPref.getString(AppConstans.LandingNative_Dashboard) == "on")
                AdsManager.inflateSplashNative(this, binding.splashNative,"SplashActivity")
        } else if (ad == AdType.ADS_RESPONSE_MATCHED) {
            logAnalytic(this, "CallBack_Matched")
            showLayout()
        }
    }

    override fun onAdEvent(eventName: String) {
        logAnalytic(this@SplashActivity, eventName)

    }
}