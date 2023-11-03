package kamai.app.ads


import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.FrameLayout
import com.applovin.sdk.AppLovinSdk
import com.facebook.ads.AdSettings
import com.google.android.gms.ads.MobileAds
import kamai.app.billing.BillingManager
import kamai.app.models.AdData
import kamai.app.enums.AdType
import kamai.app.enums.Priority
import kamai.app.interfaces.IAdListener


/**
 * Created by BiLal Zurmati on 1st of april 2022
 *
 * This is the core class which will be used by the developers for inflating interstitial ad, and native ad of both networks (Admob and Applovin)
 */
class AdsManager {

    companion object {
        var clickCounter = 0
        var isInterstitialDismissed = true
        private var splashInflated = false
        private var backInterstitialFlag = false
        private var splashInterstitialFlag = false
        private var adCallback: IAdListener? = null

        /**
         * Object of AdData class to use for different methods as it contains data for ads e.g Ad_ids, Ad_requests, Ad_Layout etc
         */
        var adData = AdData()

        var isHCPMNativeLoaded = false
        private var nativeResponseCounter = 0
        var triggerNative = true
        var isHCPMIntersLoaded = false
        var intersResponseCounter = 0
        var triggerInters = true


        /**
         * call this method to initialize admob
         */
        fun initAdmob(
            context: Context,
            initialized: (Boolean) -> Unit = {}
        ) {

//            takeConsent(context as Activity) {
            MobileAds.initialize(
                context
            ) {
                initialized.invoke(true)
            }
//            }


        }

        private fun loadNativeAds(context: Context) {
            AdmobManager.loadNativeAdHCPM1(context) {
                handleNativeAdResponse(Priority.HIGH, it)
            }
            AdmobManager.loadNativeAdMCPM1(context) {
                handleNativeAdResponse(Priority.HIGH, it)
            }
            AdmobManager.loadNativeAdLCPM(context) {
                handleNativeAdResponse(Priority.LOW, it)
            }
        }

        private fun loadInterstitialAds(context: Context) {
            AdmobManager.loadInterstitialAdHCPM1(context) {
                handleInterstitialAdResponse(Priority.HIGH, it)
            }
            AdmobManager.loadInterstitialAdMCPM1(context) {
                handleInterstitialAdResponse(Priority.HIGH, it)
            }
            AdmobManager.loadInterstitialAdLCPM(context) {
                handleInterstitialAdResponse(Priority.LOW, it)
            }
        }


        /**
         * Call this method on splash for requesting
         * Native ads (First High, First Medium and Low CPM)
         * Interstitial ads (First High, First Medium and Low CPM)
         */
        fun startRequestingAds(
            context: Context,
            _adCallback: IAdListener
        ) {
            adCallback = _adCallback
            loadNativeAds(context)
            loadInterstitialAds(context)

        }

        fun requestAppOpen(application: Application) {
            val appOpenManager =
                AppOpenManager(application) //this will start requesting for appOpen Ads
        }

        private fun handleNativeAdResponse(priority: Priority, response: Boolean) {
            if (priority == Priority.HIGH && response) {
                isHCPMNativeLoaded = true
            }

            nativeResponseCounter += 1

            if (triggerNative && (isHCPMNativeLoaded || nativeResponseCounter >= 3)) {
                triggerNative = false
                adCallback!!.onAdResponse(AdType.NATIVE_INFLATION)
            }
        }


        private fun handleInterstitialAdResponse(priority: Priority, response: Boolean) {
            if (priority == Priority.HIGH && response) {
                isHCPMIntersLoaded = true
            }

            intersResponseCounter += 1

            if (triggerInters && (isHCPMIntersLoaded || intersResponseCounter >= 3)) {
                if (isHCPMNativeLoaded || nativeResponseCounter >= 3) {
                    triggerInters = false
                    adCallback!!.onAdResponse(AdType.ADS_RESPONSE_MATCHED)
                }
            }
        }


        /**
         * Admob GDPR Consent
         *
         */
//        fun takeConsent(activity: Activity, consentEvent: (Boolean) -> Unit) {
//
//            val debugSettings = ConsentDebugSettings.Builder(activity)
//                .setDebugGeography(
//                    ConsentDebugSettings
//                        .DebugGeography
//                        .DEBUG_GEOGRAPHY_EEA
//                )
//                .addTestDeviceHashedId("TEST-DEVICE-HASHED-ID")
//                .build()
//
//            // Set tag for underage of consent. Here false means users are not underage.
//            val params = ConsentRequestParameters.Builder()
//                .setTagForUnderAgeOfConsent(true)
//                .setConsentDebugSettings(debugSettings)
//                .build()
//
//            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
//            consentInformation.requestConsentInfoUpdate(activity, params, {
//                // The consent information state was updated.
//                // You are now ready to check if a form is available.
//                if (consentInformation.isConsentFormAvailable) {
//
//                    UserMessagingPlatform.loadConsentForm(activity, { consentForm ->
//                        if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
//                            consentForm.show(activity) { formError ->
//                                consentEvent.invoke(true)
//                            }
//                        }
//                    },
//                        { formError ->
//                            consentEvent.invoke(false)
//                        }
//                    )
//                }
//            },
//                { formError ->
//                    consentEvent.invoke(false)
//                }
//            )
//
//        }


        /**
         * method to check each instance every time and fill the inventary if any instance is found null or empty
         */
        private fun checkInstances(activity: Activity) {
            if (AdmobManager.nativeAdHCPM1 == null)
                AdmobManager.loadNativeAdHCPM1(activity) {}

            if (AdmobManager.nativeAdHCPM2 == null)
                AdmobManager.loadNativeAdHCPM2(activity) {}

            if (AdmobManager.nativeAdMCPM1 == null)
                AdmobManager.loadNativeAdMCPM1(activity) {}

            if (AdmobManager.nativeAdMCPM2 == null)
                AdmobManager.loadNativeAdMCPM2(activity) {}

            if (AdmobManager.nativeAdLCPM == null)
                AdmobManager.loadNativeAdLCPM(activity) {}

            if (AdmobManager.interstitialAdHCPM1 == null)
                AdmobManager.loadInterstitialAdHCPM1(activity) {}

            if (AdmobManager.interstitialAdHCPM2 == null)
                AdmobManager.loadInterstitialAdHCPM2(activity) {}

            if (AdmobManager.interstitialAdMCPM1 == null)
                AdmobManager.loadInterstitialAdMCPM1(activity) {}

            if (AdmobManager.interstitialAdMCPM2 == null)
                AdmobManager.loadInterstitialAdMCPM2(activity) {}

            if (AdmobManager.interstitialAdLCPM == null)
                AdmobManager.loadInterstitialAdLCPM(activity) {}

        }


        /**
         * This method can be called on splash for inflating the native ad for splash, but make sure to call this method when you get NativeCallback from AdResponse
         */
        fun inflateSplashNative(
            activity: Activity,
            nativeContainer: FrameLayout,
            callingPlaceName: String
        ) {
            AdsHelper.initResources(activity, nativeContainer, "withOutNoAds", callingPlaceName)
            if (AdmobManager.nativeAdHCPM1 != null || AdmobManager.nativeAdMCPM1 != null || AdmobManager.nativeAdLCPM != null) {
                AdmobManager.showNativeWithoutNoAds(activity, nativeContainer, callingPlaceName)
            } else if (AdmobManager.tempNative != null) {
                AdmobManager.showNativeWithoutNoAds(activity, nativeContainer, callingPlaceName)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}

            } else {
                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}


            }
        }


        /**
         * Call this method wherever you want to show interstitial and this method will also take care to of the click capping
         */
        fun showInterstitialWithClick(
            activity: Activity,
            adEvent: (Boolean) -> Unit = {}
        ) {

            checkInstances(activity)

            if (!splashInterstitialFlag) {
                splashInterstitialFlag = true
                showInterstitial(activity, adEvent)
            } else {

                clickCounter += 1
                if (clickCounter % adData.CappingCounter != 0) {
                    adEvent.invoke(false)
                } else
                    showInterstitial(activity, adEvent)
            }


        }

        /**
         * This method will check if we have loaded instance of admob available to inflate
         */
        private fun isAdmobInterstitialAvailable(): Boolean {
            return AdmobManager.interstitialAdHCPM1 != null || AdmobManager.interstitialAdHCPM2 != null || AdmobManager.interstitialAdMCPM1 != null || AdmobManager.interstitialAdMCPM2 != null || AdmobManager.interstitialAdLCPM != null
        }

        /**
         * Call this method wherever you want to show interstitial but this method won't calculate click capping
         */
        fun showInterstitial(activity: Activity, adEvent: (Boolean) -> Unit = {}) {
            if (adData.inApp) //an extra layer if this method is invoked directly from outside the class
                adEvent.invoke(false)
            else if (isAdmobInterstitialAvailable()) {
                AdmobManager.showInterstitialAd(activity, adEvent)
            } else {

                BillingManager.showPremium(activity) {
                    adEvent.invoke(it)
                }


                if (!AdmobManager.interstitialAdLoadHCPM1)
                    AdmobManager.loadInterstitialAdHCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadHCPM2)
                    AdmobManager.loadInterstitialAdHCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM1)
                    AdmobManager.loadInterstitialAdMCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM2)
                    AdmobManager.loadInterstitialAdMCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadLCPM)
                    AdmobManager.loadInterstitialAdLCPM(activity) {}
            }
        }

        /**
         * This method will only calculate the capping and set the flag if the capping matches
         * This is an extra method made so that we show an interstitial ad onBackPress
         */
        fun countInterstitialCapping(activity: Activity) {
            checkInstances(activity)
            clickCounter += 1
            if (clickCounter % adData.CappingCounter == 0) {
                backInterstitialFlag = true
            }
        }


        /**
         * Call this method wherever you want to show interstitial on back press but make sure to call this method onCreate of new fragment
         */
        fun showInterstitialOnBack(activity: Activity, adEvent: (Boolean) -> Unit = {}) {
            if (adData.inApp) //an extra layer if this method is invoked directly from outside the class
                adEvent.invoke(false)
            else if (!backInterstitialFlag)
                adEvent.invoke(false)
            else if (isAdmobInterstitialAvailable()) {
                AdmobManager.showInterstitialAd(activity, adEvent)
            } else {

                BillingManager.showPremium(activity) {
                    adEvent.invoke(it)
                }


                if (!AdmobManager.interstitialAdLoadHCPM1)
                    AdmobManager.loadInterstitialAdHCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadHCPM2)
                    AdmobManager.loadInterstitialAdHCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM1)
                    AdmobManager.loadInterstitialAdMCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM2)
                    AdmobManager.loadInterstitialAdMCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadLCPM)
                    AdmobManager.loadInterstitialAdLCPM(activity) {}
            }

            backInterstitialFlag = false
        }

        /**
         * Call this method wherever you want to show interstitial without loading and this method won't calculate click capping
         */
        fun showSplashInterstitial(activity: Activity, adEvent: (Boolean) -> Unit = {}) {
            if (adData.inApp) //an extra layer if this method is invoked directly from outside the class
                adEvent.invoke(false)
            else if (splashInterstitialFlag)
                adEvent.invoke(false)
            else if (isAdmobInterstitialAvailable()) {
                splashInterstitialFlag = true
                AdmobManager.showInterstitialForSplash(activity, adEvent)
            } else {

                BillingManager.showPremium(activity) {
                    adEvent.invoke(it)
                }


                if (!AdmobManager.interstitialAdLoadHCPM1)
                    AdmobManager.loadInterstitialAdHCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadHCPM2)
                    AdmobManager.loadInterstitialAdHCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM1)
                    AdmobManager.loadInterstitialAdMCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM2)
                    AdmobManager.loadInterstitialAdMCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadLCPM)
                    AdmobManager.loadInterstitialAdLCPM(activity) {}
            }
        }


        /**
         * This method will return callback when interstitial is shown to user
         * Click counter is also applied to it
         */
        fun showIntersWithClick(
            activity: Activity,
            adEvent: (Boolean) -> Unit = {}
        ) {
            checkInstances(activity)

            if (!splashInterstitialFlag) {
                splashInterstitialFlag = true
                showInters(activity, adEvent)
            } else {
                clickCounter += 1
                if (clickCounter % adData.CappingCounter != 0) {
                    adEvent.invoke(false)
                } else
                    showInters(activity, adEvent)
            }


        }

        /**
         * Call this method wherever you want to show interstitial but this method won't calculate click capping
         * This method will return callback when interstitial is shown to user
         */
        fun showInters(activity: Activity, adEvent: (Boolean) -> Unit = {}) {
            if (adData.inApp)
                adEvent.invoke(false)
            else if (isAdmobInterstitialAvailable()) {
                AdmobManager.showIntersAd(activity, adEvent)
            } else {

                BillingManager.showScreen(activity) {
                    adEvent.invoke(it)
                }


                if (!AdmobManager.interstitialAdLoadHCPM1)
                    AdmobManager.loadInterstitialAdHCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadHCPM2)
                    AdmobManager.loadInterstitialAdHCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM1)
                    AdmobManager.loadInterstitialAdMCPM1(activity) {}

                if (!AdmobManager.interstitialAdLoadMCPM2)
                    AdmobManager.loadInterstitialAdMCPM2(activity) {}

                if (!AdmobManager.interstitialAdLoadLCPM)
                    AdmobManager.loadInterstitialAdLCPM(activity) {}

            }
        }


        /**
         * This method will check if we have loaded instance of admob available to inflate
         */
        private fun isAdmobNativeAvailable(): Boolean {
            return AdmobManager.nativeAdHCPM1 != null || AdmobManager.nativeAdHCPM2 != null || AdmobManager.nativeAdMCPM1 != null || AdmobManager.nativeAdMCPM2 != null || AdmobManager.nativeAdLCPM != null
        }

        /**
         * Call this method wherever you want to show native ad
         * simply pass activity, frame layout and the class where we are calling this method as a parameter
         */
        fun showNativeAd(
            activity: Activity,
            container: FrameLayout,
            callingPlaceName: String
        ) {
            AdsHelper.initResources(activity, container, "withNoAds", callingPlaceName)
            if (isAdmobNativeAvailable()) {
                AdmobManager.showNative(activity, container, callingPlaceName)
            } else if (AdmobManager.tempNative != null) {
                AdmobManager.showNative(activity, container, callingPlaceName)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}


            } else {
                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}


            }

        }


        /**
         * Call this method wherever you want to show native ad
         * This method wont show no ads button in layout, this method was added in support with small native ads, like ads with only icon or media etc
         * simply pass activity, frame layout and the class where we are calling this method as a parameter
         */
        fun showSmallNative(
            activity: Activity,
            container: FrameLayout,
            callingPlaceName: String,
            adLoaded: (Boolean) -> Unit = {}
        ) {
            AdsHelper.initResources(activity, container, "withNoAds", callingPlaceName)
            if (isAdmobNativeAvailable()) {
                AdmobManager.showSmallNative(activity, container, callingPlaceName, adLoaded)
            } else if (AdmobManager.tempNative != null) {
                AdmobManager.showRVNative(activity, container, callingPlaceName, adLoaded)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}

            } else {

                adLoaded.invoke(false)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}

            }

        }

        /**
         * Call this method wherever you want to show native ad without No ads button
         * simply pass activity, frame layout and the class where we are calling this method as a parameter
         */
        fun showNativeAdWithoutNoAds(
            activity: Activity,
            container: FrameLayout,
            callingPlaceName: String
        ) {
            AdsHelper.initResources(activity, container, "withOutNoAds", callingPlaceName)
            if (isAdmobNativeAvailable()) {
                AdmobManager.showNativeWithoutNoAds(activity, container, callingPlaceName)
            } else if (AdmobManager.tempNative != null) {
                AdmobManager.showNativeWithoutNoAds(activity, container, callingPlaceName)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}

            } else {
                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}
            }

        }

        /**
         * Call this method when you want to show native ad in recyclerView
         * simply pass activity, frame layout and the class where we are calling this method as a parameter
         * This method will return true if an ad was available and false if an ad wasn't available
         * Handle the Container's visibility in recyclerView when you get true of false value
         */
        fun showRvNativeAd(
            activity: Activity,
            container: FrameLayout,
            callingPlaceName: String,
            adLoaded: (Boolean) -> Unit = {}
        ) {
            AdsHelper.initResources(activity, container, "withNoAds", callingPlaceName)
            if (isAdmobNativeAvailable()) {
                AdmobManager.showRVNative(activity, container, callingPlaceName, adLoaded)
            } else if (AdmobManager.tempNative != null) {
                AdmobManager.showRVNative(activity, container, callingPlaceName, adLoaded)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}

            } else {

                adLoaded.invoke(false)

                if (!AdmobManager.nativeLoadingHCPM1)
                    AdmobManager.loadNativeAdHCPM1(activity) {}

                if (!AdmobManager.nativeLoadingHCPM2)
                    AdmobManager.loadNativeAdHCPM2(activity) {}

                if (!AdmobManager.nativeLoadingMCPM1)
                    AdmobManager.loadNativeAdMCPM1(activity) {}

                if (!AdmobManager.nativeLoadingMCPM2)
                    AdmobManager.loadNativeAdMCPM2(activity) {}

                if (!AdmobManager.nativeLoadingLCPM)
                    AdmobManager.loadNativeAdLCPM(activity) {}

            }

        }

        /**
         * Call this method when you want to null all the instances
         * ideal case is when user purchase in app
         */
        fun clearInstances() {
            splashInflated = false
            AdmobManager.nativeAdHCPM1 = null
            AdmobManager.nativeAdHCPM2 = null
            AdmobManager.nativeAdMCPM1 = null
            AdmobManager.nativeAdMCPM2 = null
            AdmobManager.nativeAdLCPM = null
            AdmobManager.tempNative = null
            AdmobManager.interstitialAdHCPM1 = null
            AdmobManager.interstitialAdHCPM2 = null
            AdmobManager.interstitialAdMCPM1 = null
            AdmobManager.interstitialAdMCPM2 = null
            AdmobManager.interstitialAdLCPM = null
            AdmobManager.admobNativeRequestCounter = 0
            AdmobManager.admobInterstitialRequestCounter = 0
            AdmobManager.admobBannerRequestCounter = 0
            clickCounter = 0
            adCallback = null

            splashInflated = false
            backInterstitialFlag = false
            splashInterstitialFlag = false
            isInterstitialDismissed=true
            isHCPMNativeLoaded = false
            nativeResponseCounter = 0
            triggerNative = true
            isHCPMIntersLoaded = false
            intersResponseCounter = 0
            triggerInters = true
        }
    }
}

