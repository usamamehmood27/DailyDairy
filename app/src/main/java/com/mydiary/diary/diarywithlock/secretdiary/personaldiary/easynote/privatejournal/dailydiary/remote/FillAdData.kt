package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.DailyDiary.BuildConfig
import com.example.DailyDiary.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.activities.SplashActivity.Companion.key
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.Debug_Events
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.InApp_Update_CT
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.LandingNative_Dashboard
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.LoadingAd_Duration
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.NativeAd_Layout_json
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.isOn
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.noAdsButton
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.AppConstans.Companion.splash_interstitial_ads
import kamai.app.ads.AdsManager
import kamai.app.models.AdData
import org.json.JSONObject

class FillAdData {
    companion object {
        var isFeatched = false
        val adData = AdData()
        fun remoteConfig(context: Context) {
            val remoteConfig = Firebase.remoteConfig
            val remoteConfigSetting = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0L
            }
            remoteConfig.setConfigSettingsAsync(remoteConfigSetting)
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        isFeatched = true
                        if (BuildConfig.DEBUG) {
                            SharedPref.putString(AppConstans.bannerId1, context.getString(R.string.admob_banner_id1))
                            SharedPref.putString(AppConstans.bannerId2, context.getString(R.string.admob_banner_id2))
                            SharedPref.putString(AppConstans.bannerId3, context.getString(R.string.admob_banner_id3))
                            SharedPref.putString(AppConstans.bannerId4, context.getString(R.string.admob_banner_id4))
                            SharedPref.putString(AppConstans.bannerId5, context.getString(R.string.admob_banner_id5))
                            SharedPref.putString(AppConstans.nativeId1, context.getString(R.string.admob_native_id1))

                            SharedPref.putString(AppConstans.nativeId2, context.getString(R.string.admob_native_id2))
                            SharedPref.putString(AppConstans.nativeId3, context.getString(R.string.admob_native_id3))
                            SharedPref.putString(AppConstans.nativeId4, context.getString(R.string.admob_native_id4))
                            SharedPref.putString(AppConstans.nativeId5, context.getString(R.string.admob_native_id5))
                            SharedPref.putString(AppConstans.intersitialId1, context.getString(R.string.admob_intersitial_id1))

                            SharedPref.putString(AppConstans.intersitialId2, context.getString(R.string.admob_intersitial_id2))
                            SharedPref.putString(AppConstans.intersitialId3, context.getString(R.string.admob_intersitial_id3))
                            SharedPref.putString(AppConstans.intersitialId4, context.getString(R.string.admob_intersitial_id4))
                            SharedPref.putString(AppConstans.intersitialId5, context.getString(R.string.admob_intersitial_id5))


                        } else {
                            SharedPref.putString(AppConstans.bannerId1, remoteConfig.getString(AppConstans.bannerId1))
                            SharedPref.putString(AppConstans.bannerId2, remoteConfig.getString(AppConstans.bannerId2))
                            SharedPref.putString(AppConstans.bannerId3, remoteConfig.getString(AppConstans.bannerId3))
                            SharedPref.putString(AppConstans.bannerId4, remoteConfig.getString(AppConstans.bannerId4))
                            SharedPref.putString(AppConstans.bannerId5, remoteConfig.getString(AppConstans.bannerId5))

                            SharedPref.putString(AppConstans.nativeId1, remoteConfig.getString(AppConstans.nativeId1))
                            SharedPref.putString(AppConstans.nativeId2, remoteConfig.getString(AppConstans.nativeId2))
                            SharedPref.putString(AppConstans.nativeId3, remoteConfig.getString(AppConstans.nativeId3))
                            SharedPref.putString(AppConstans.nativeId4, remoteConfig.getString(AppConstans.nativeId4))
                            SharedPref.putString(AppConstans.nativeId5, remoteConfig.getString(AppConstans.nativeId5))

                            SharedPref.putString(AppConstans.intersitialId1, remoteConfig.getString(AppConstans.intersitialId1))
                            SharedPref.putString(AppConstans.intersitialId2, remoteConfig.getString(AppConstans.intersitialId2))
                            SharedPref.putString(AppConstans.intersitialId3, remoteConfig.getString(AppConstans.intersitialId3))
                            SharedPref.putString(AppConstans.intersitialId4, remoteConfig.getString(AppConstans.intersitialId4))
                            SharedPref.putString(AppConstans.intersitialId5, remoteConfig.getString(AppConstans.intersitialId5))
                        }

                        // get the Vaue


                        val debug_Events = remoteConfig.getString(Debug_Events)
                        val noads_button = remoteConfig.getString(noAdsButton)
                        val splashHInterstitialAd = remoteConfig.getString(splash_interstitial_ads)
                        val ad_loading_duration = remoteConfig.getString(LoadingAd_Duration)
                        val splashNativeAd = remoteConfig.getString(LandingNative_Dashboard)
                        val nativeAd_Layout_json = remoteConfig.getString(NativeAd_Layout_json)
                        Log.d("khan"," Int:$splashHInterstitialAd")
                        Log.d("khan","Native:$splashNativeAd")
                        Log.d("khan","Event:$debug_Events")
                        Log.d("khan","Noadsbutton:$noads_button")
                        Log.d("khan","Loading:$ad_loading_duration")


                        // Put the value
                        SharedPref.putString(Debug_Events, debug_Events)
                        SharedPref.putString(noAdsButton, noads_button)
                        SharedPref.putString(splash_interstitial_ads, splashHInterstitialAd)
                        SharedPref.putString(LoadingAd_Duration, ad_loading_duration)
                        SharedPref.putString(LandingNative_Dashboard, splashNativeAd )
                        SharedPref.putString(NativeAd_Layout_json, nativeAd_Layout_json)


                        SharedPref.putInt(AppConstans.BatchScan, getValue(remoteConfig.getString(AppConstans.BatchScan)))
                        SharedPref.putString(AppConstans.UXCam, remoteConfig.getString(AppConstans.UXCam))
                        getAdmobCtaColors(remoteConfig.getString(AppConstans.Applovin_layoutTemplate))
//                        getApplovinCtaColors(remoteConfig.getString(AppConstans.searchJsonapplovintextcolor))
                        SharedPref.putInt(AppConstans.CappingCounter, getValue(remoteConfig.getString(AppConstans.CappingCounter)))
                        SharedPref.putInt(AppConstans.nativeRequests, getValue(remoteConfig.getString(AppConstans.nativeRequests)))
                        SharedPref.putInt(AppConstans.bannerRequests, getValue(remoteConfig.getString(AppConstans.bannerRequests)))
                        SharedPref.putInt(AppConstans.interstitialRequests, getValue(remoteConfig.getString(AppConstans.interstitialRequests)))
                        setValue(context)

                    }
                }
        }

        private fun getValue(counter: String): Int {
            return if (counter == "")
                0
            else
                counter.toInt()
        }

        private fun getApplovinCtaColors(json: String) {
            val json = JSONObject(json)
            val cta = json.getString("cta")
            val text = json.getString("text")
            val applovinCtaColor = JSONObject(cta).getString("color")
            val applovinCtaTextColor = JSONObject(text).getString("color")
            SharedPref.putString(AppConstans.ApplovinCtaColor, applovinCtaColor)
            SharedPref.putString(AppConstans.ApplovinCtaTextColor, applovinCtaTextColor)
            Log.d("Color","CTA: ${applovinCtaColor } :Textcolor:${applovinCtaTextColor}")

        }
        private fun getAdmobCtaColors(json: String) {
            val json = JSONObject(json)
            val cta = json.getString("cta")
            val text = json.getString("text")
            val admobCtaColor = JSONObject(cta).getString("color")
            val admobCtaTextColor = JSONObject(text).getString("color")
            SharedPref.putString(AppConstans.ApplovinCtaColor, admobCtaColor)
            SharedPref.putString(AppConstans.ApplovinCtaTextColor, admobCtaTextColor)
            Log.d("Color","CTA: ${admobCtaColor} :Textcolor:${admobCtaTextColor}")


        }



        fun setValue(context: Context) {

            val cacheDir = context.cacheDir
            if (SharedPref.getBolean("clear", false)) {
                if (cacheDir.exists()) {
                    cacheDir.deleteRecursively()
                }
            }

            adData.inApp = SharedPref.getBolean(AppConstans.inApp, false)

            adData.BannerId1 = SharedPref.getString(AppConstans.bannerId1, context.getString(R.string.admob_banner_id1))
            adData.BannerId2 = SharedPref.getString(AppConstans.bannerId2, context.getString(R.string.admob_banner_id2))
            adData.BannerId3 = SharedPref.getString(AppConstans.bannerId3, context.getString(R.string.admob_banner_id3))
            adData.BannerId4 = SharedPref.getString(AppConstans.bannerId4, context.getString(R.string.admob_banner_id4))
            adData.BannerId5 = SharedPref.getString(AppConstans.bannerId5, context.getString(R.string.admob_banner_id5))


            adData.nativeIdHCPM1 = SharedPref.getString(AppConstans.nativeId1, context.getString(R.string.admob_native_id1))
            adData.nativeIdHCPM2 = SharedPref.getString(AppConstans.nativeId2, context.getString(R.string.admob_native_id2))
            adData.nativeIdMCPM1 = SharedPref.getString(AppConstans.nativeId3, context.getString(R.string.admob_native_id3))
            adData.nativeIdMCPM2 = SharedPref.getString(AppConstans.nativeId4, context.getString(R.string.admob_native_id4))
            adData.nativeIdLCPM = SharedPref.getString(AppConstans.nativeId5, context.getString(R.string.admob_native_id5))

            adData.InterstitialIdHCPM1 = SharedPref.getString(AppConstans.intersitialId1, context.getString(R.string.admob_intersitial_id1))
            adData.InterstitialIdHCPM2 = SharedPref.getString(AppConstans.intersitialId2, context.getString(R.string.admob_intersitial_id2))
            adData.InterstitialIdMCPM1 = SharedPref.getString(AppConstans.intersitialId3, context.getString(R.string.admob_intersitial_id3))
            adData.InterstitialIdMCPM2 = SharedPref.getString(AppConstans.intersitialId4, context.getString(R.string.admob_intersitial_id4))
            adData.InterstitialIdLCPM = SharedPref.getString(AppConstans.intersitialId5, context.getString(R.string.admob_intersitial_id5))

            Log.d("zaid","${adData.InterstitialIdLCPM}")
            adData.AdmobCtaColor = SharedPref.getString(AppConstans.AdmobCtaColor,"#000000")
            adData.AdmobCtaTextColor = SharedPref.getString(AppConstans.AdmobCtaTextColor,"#ffffff")


            // new code
            adData.classesJson = SharedPref.getString(NativeAd_Layout_json, "")
            adData.reportClassName = SharedPref.getString(Debug_Events, "")
            adData.appUpdateType = SharedPref.getString(InApp_Update_CT, "1")
            adData.noAdsButton = SharedPref.getString(noAdsButton, "")
            adData.SplashInterstitialAd = SharedPref.getString(splash_interstitial_ads, "off")
            adData.loadingDuration = SharedPref.getString(LoadingAd_Duration, "1500")
            adData.SplashNativeAd =SharedPref.getString(LandingNative_Dashboard, "off")

            adData.ApplovinCtaColor = SharedPref.getString(AppConstans.ApplovinCtaColor)
            adData.ApplovinCtaTextColor = SharedPref.getString(AppConstans.ApplovinCtaTextColor)
            Log.d("ndundwjufndwncdnc", "setValue: " + SharedPref.getString(AppConstans.ApplovinCtaColor))
            Log.d("ndundwjufndwncdnc", "setValue: " +  SharedPref.getString(AppConstans.ApplovinCtaTextColor))
            key=SharedPref.getString(AppConstans.UXCam,"lf44fx5w9e97ake")
            isOn = "on"
            adData.CappingCounter = SharedPref.getInt(AppConstans.CappingCounter, 3)
            adData.interstitialRequests = SharedPref.getInt(AppConstans.interstitialRequests, 50)
            adData.nativeRequests = SharedPref.getInt(AppConstans.nativeRequests, 50)
            adData.bannerRequests = SharedPref.getInt(AppConstans.bannerRequests, 50)

            AdsManager.adData = adData




//            AdsManager.adData.apply {
//                inApp = SharedPref.getBolean(AppConstans.inApp, false)
//                layoutTemplate = SharedPref.getString(AppConstans.Applovin_layoutTemplate)
//                noAdsButton = SharedPref.getString(AppConstans.noAdsButton, "off")
//                reportClassName = SharedPref.getString(AppConstans.reportClassName, "off")
//                classesJson = SharedPref.getString(AppConstans.classesJson)
//                key=SharedPref.getString(AppConstans.UXCam,"lf44fx5w9e97ake")
//                interstitialLoadingTime=SharedPref.getString(AppConstans.LoadingAd_Duration,"1500")
//                ApplovinCtaColor = SharedPref.getString(AppConstans.ApplovinCtaColor)
//                ApplovinCtaTextColor = SharedPref.getString(AppConstans.ApplovinCtaTextColor)
//                isSplashInterstitialOn = SharedPref.getString(AppConstans.LandingIntersitial_Dashboard)
//                isOn = "on"
//                CappingCounter = SharedPref.getInt(AppConstans.CappingCounter, 3)
//            }

        }
    }
}