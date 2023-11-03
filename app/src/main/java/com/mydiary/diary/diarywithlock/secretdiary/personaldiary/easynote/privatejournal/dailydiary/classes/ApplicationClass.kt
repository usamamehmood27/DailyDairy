package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.remote.FillAdData
import kamai.app.ads.AdsManager
import kamai.app.update.InAppUpdate
import java.util.Locale

class ApplicationClass: Application() {
    companion object{
        var instance:Application?=null
        fun getApplication():Application?{
            return instance
        }
        fun getcontext(): Context {
            return instance!!.applicationContext
        }
    }
    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, Locale.ENGLISH)
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(baseContext, super.getResources())
    }

    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
        instance=this
        FillAdData.setValue(this)
        AdsManager.initAdmob(this) {}
//        AdsManager.initAdsModule(this) {}
//        InAppUpdate.init(this)
      //  AdsManager.requestAppOpen(this)
    }
}