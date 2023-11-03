package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.activities


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.ActivityMainBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.NotesDatabase
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.Alarm_Alert
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.SplashAd
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.exitDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.alarmCallBack
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.back
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.enterenceback
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.logAnalytic
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.splashcallback
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.MainViewModelFactory
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.NotesRepository
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
import kamai.app.ads.AdsHelper
import kamai.app.ads.AdsManager
import kamai.app.interfaces.IEventListener
import kamai.app.interfaces.IUpdateListener
import kamai.app.update.InAppUpdate

class MainActivity :  LocalizationActivity(), Alarm_Alert, SplashAd, IEventListener,
    IUpdateListener {
    lateinit var viewmodel:Notes_ViewModel
    companion object{
        lateinit var  noteRepository:NotesRepository
    }

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val contoller by lazy {
        val navhostFragemnt =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navhostFragemnt.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        alarmCallBack=this
        splashcallback=this
        AdsHelper.eventListener = this

        checkForUpdates()

        setUpViewModel()
        contoller.addOnDestinationChangedListener() { controller, destination, arguments ->
            Handler(Looper.getMainLooper()).postDelayed({
                AdsManager.showInterstitialOnBack(this) {
                }
            }, 250)
        }
    }

  fun checkForUpdates() {
      InAppUpdate.init(this)
      InAppUpdate.registerListener(this)
      InAppUpdate.checkAppUpdate(this)
    }

    private fun setUpViewModel() {
         noteRepository = NotesRepository(
            NotesDatabase(this)
        )

        val MainviewModelFactory =
            MainViewModelFactory(
                application,
                noteRepository
            )
        viewmodel = ViewModelProvider(
            this,
            MainviewModelFactory
        )[Notes_ViewModel::class.java]
    }
    override fun onBackPressed() {
        if (contoller.currentDestination!!.id==R.id.dashboardFragment){
            exitDialog(this)
        }else if (contoller.currentDestination!!.id==R.id.dairyPageFragment){
            if (checkingState=="CreateNote"){
                back.click()
                AdsManager.countInterstitialCapping(this)
            }
            else{
                AdsManager.countInterstitialCapping(this)
                super.onBackPressed()
            }
        }
        else if (contoller.currentDestination!!.id==R.id.lockFragment){
            SharedPref.putString("password","")
            SharedPref.putBolean("lock", false)
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.saveNotes_Fragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.clanderFragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.settingFragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.draft_Fragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.folders_Fragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.languageFragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.sildeMenuFragment){
            AdsManager.countInterstitialCapping(this)
            super.onBackPressed()
        }
        else if (contoller.currentDestination!!.id==R.id.enterence_Fragment){
            enterenceback.onBack()
        }
        else{
            super.onBackPressed()
        }


    }

    override fun alarm() {
        showToast(this, "Alarm")
    }

    override fun onClick() {
        Handler(Looper.getMainLooper()).postDelayed({
            AdsManager.showSplashInterstitial(this) {
                InAppUpdate.checkAppUpdate(this) {}
            }
        }, 250)
    }

    override fun onAdEvent(eventName: String) {
        logAnalytic(this@MainActivity, eventName)
    }

    override fun updateDownloaded() {
        InAppUpdate.showUserConsentDialog(this@MainActivity, binding.root)
    }
}