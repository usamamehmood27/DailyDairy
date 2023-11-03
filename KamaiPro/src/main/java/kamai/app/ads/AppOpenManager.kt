package kamai.app.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import kamai.app.ads.AdsManager.Companion.adData
import kamai.app.ads.AdsManager.Companion.isInterstitialDismissed
import kamai.app.enums.AdType


/**
 * Created by Bilal Zurmati 5-May-2023
 *
 * This class needs modification and for now leaving it as it is.
 *
 */

class AppOpenManager(application: Application) : Application.ActivityLifecycleCallbacks,
    LifecycleEventObserver {
    private var appOpenAdRequested = false
    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false


    private var myApplication: Application? = null


    init {
        this.myApplication = application
        this.myApplication!!.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    /**
     * Request an ad
     */
    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (notRequestNextAd()) {
            return
        }

        appOpenAdRequested = true

        val loadCallback = object : AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(p0: AppOpenAd) {
                super.onAdLoaded(p0)
                appOpenAdRequested = false
                appOpenAd = p0
                Log.i("MyKey", "onStateChanged:AppOpen loaded")
                Log.i("APOPENADHERE", "onStateChanged: loaded")


            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                appOpenAdRequested = false
                Log.d("MyKey", "errorAppOpen $p0")
            }

        }


        AppOpenAd.load(
            myApplication!!, adData.AppOpenAdId, getAdRequest(), loadCallback
        )
        appOpenAdRequested = true
        Log.i("APOPENADHERE", "onStateChanged: request")

    }


    /**
     * Shows the ad if one isn't already showing.
     */
    fun showAdIfAvailable(listener: (Boolean) -> Unit = {}) {

        //we won't load or show AppOpenAd if its paid user.
        if (adData.inApp || !isInterstitialDismissed)
            return

        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d("LOG_TAG", "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        listener.invoke(true)
                        fetchAd()
                    }


                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        AdmobManager.trackAdEvents(
                            "Splash_APP_OPEN_Clicked",
                            "Admob_APP_OPEN_Clicked"
                        )
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        AdmobManager.trackAdEvents(
                            "Splash_APP_OPEN_Impression",
                            "Admob_APP_OPEN_Impression"
                        )

                    }

                }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(currentActivity!!)
        } else {
            listener.invoke(false)
            Log.d("LOG_TAG", "Can not show ad.")
            fetchAd()
        }
    }

    /**
     * Creates and returns ad request.
     */
    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    private fun notRequestNextAd(): Boolean {
        return appOpenAd != null || appOpenAdRequested
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
    }


    /** LifecycleObserver methods  */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            Log.i("MyKey", "onStateChanged: method started")
            showAdIfAvailable()
        }
    }

}