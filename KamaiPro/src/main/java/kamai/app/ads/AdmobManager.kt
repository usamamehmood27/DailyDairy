package kamai.app.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import kamai.app.ads.AdsHelper.Companion.isNetworkAvailable
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import kamai.app.R
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kamai.app.ads.AdsHelper.Companion.getColorValue
import kamai.app.ads.AdsHelper.Companion.getLayout
import kamai.app.ads.AdsManager.Companion.adData
import kamai.app.billing.BillingManager
import kamai.app.databinding.LayoutAdLoadingBinding
import kamai.app.databinding.LayoutNativeAdViewBinding
import kamai.app.ads.AdsHelper.Companion.eventListener
import kamai.app.ads.AdsHelper.Companion.getLayoutType
import kamai.app.enums.Layout

/**
 *  Created by BiLal Zurmati on 30 june 2022
 */
class AdmobManager {
    companion object {

        //Native add
        var tempNative: NativeAd? = null
        var nativeAdHCPM1: NativeAd? = null
        var nativeAdHCPM2: NativeAd? = null
        var nativeAdMCPM1: NativeAd? = null
        var nativeAdMCPM2: NativeAd? = null
        var nativeAdLCPM: NativeAd? = null

        var nativeLoadingHCPM1 = false
        var nativeLoadingHCPM2 = false
        var nativeLoadingMCPM1 = false
        var nativeLoadingMCPM2 = false
        var nativeLoadingLCPM = false
        var admobNativeRequestCounter = 0
        var admobBannerRequestCounter = 0
        var admobInterstitialRequestCounter = 0

        /**
         * What conditions do we check here
         * if all of the ads are turned off
         * if Internet is not working
         * Check if user is premium or not
         * Check available requests we have
         */
        private fun isNativeAdLoadingNotPermissible(context: Context): Boolean {
            return adData.isOn.lowercase() == "off" || !isNetworkAvailable(context) || adData.inApp || adData.nativeRequests <= admobNativeRequestCounter
        }

        private fun getAdLoader(
            context: Context,
            nativeAdId: String,
            loadEvent: (NativeAd) -> Unit
        ): AdLoader.Builder {
            return AdLoader.Builder(context, nativeAdId).forNativeAd { nativeAd ->
                loadEvent.invoke(nativeAd)
            }
        }

        fun trackAdEvents(vararg events: String) {
            for (event in events) {
                eventListener.onAdEvent(event)
                Log.i("MyKey", "loadNativeAd1: calling $event")
            }
        }

        /**
         * High CPM
         */
        fun loadNativeAdHCPM1(context: Context, adEvent: (Boolean) -> Unit) {

            if (nativeLoadingHCPM1 || isNativeAdLoadingNotPermissible(context)) {
                return
            }

            /**
             * this code will execute only if none of the above condition matches.
             */
            admobNativeRequestCounter++
            nativeLoadingHCPM1 = true


            val adLoader = getAdLoader(context, adData.nativeIdHCPM1) { nativeAd ->
                nativeAdHCPM1 = nativeAd
            }

            adLoader.withAdListener(object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.i("MyKey", "loadNativeAdHCPM1: Ad loaded")
                    nativeLoadingHCPM1 = false
                    adEvent.invoke(true)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i("MyKey", "loadNativeAdHCPM1: ${adError.code}")
                    nativeLoadingHCPM1 = false
                    adEvent.invoke(false)

                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    if (nativeAdHCPM1 != null) {
                        tempNative = nativeAdHCPM1
                        nativeAdHCPM1 = null

                    }

                    trackAdEvents("Splash_Native_Impression", "Admob_Native_Impression")

                    Log.i("MyKey", "loadNativeAdHCPM1: calling impression")

                    loadNativeAdHCPM1(context) {}
                }

                override fun onAdClicked() {
                    super.onAdClicked()

                    trackAdEvents("Splash_Native_Clicked", "Admob_Native_Clicked")

                    inflateNextFreshNative()

                }


            })


            adLoader.build().loadAd(AdRequest.Builder().build())
        }

        private fun inflateNextFreshNative() {
            Handler(Looper.getMainLooper()).postDelayed({
                if (AdsHelper.getContext() != null && AdsHelper.getNativeContainer() != null) {
                    if (AdsHelper.adTypeWithAds == "withNoAds") {
                        showNative(
                            AdsHelper.getContext()!!,
                            AdsHelper.getNativeContainer()!!,
                            AdsHelper.getLastNativeAdPlaceName()
                        )
                    } else {
                        showNativeWithoutNoAds(
                            AdsHelper.getContext()!!,
                            AdsHelper.getNativeContainer()!!,
                            AdsHelper.getLastNativeAdPlaceName()
                        )
                    }
                }
            }, 200)
        }


        fun loadNativeAdHCPM2(context: Context, adEvent: (Boolean) -> Unit) {

            if (nativeLoadingHCPM2 || isNativeAdLoadingNotPermissible(context)) {
                return
            }

            /**
             * this code will execute only if none of the above condition matches.
             */
            admobNativeRequestCounter++
            nativeLoadingHCPM2 = true


            val adLoader = getAdLoader(context, adData.nativeIdHCPM2) { nativeAd ->
                nativeAdHCPM2 = nativeAd
            }

            adLoader.withAdListener(object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.i("MyKey", "loadNativeAdHCPM2: Ad loaded")
                    nativeLoadingHCPM2 = false
                    adEvent.invoke(true)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i("MyKey", "loadNativeAdHCPM2: ${adError.code}")
                    nativeLoadingHCPM2 = false
                    adEvent.invoke(false)

                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    if (nativeAdHCPM2 != null) {
                        tempNative = nativeAdHCPM2
                        nativeAdHCPM2 = null

                    }

                    loadNativeAdHCPM2(context) {}

                    trackAdEvents("Splash_Native_Impression", "Admob_Native_Impression")


                    Log.i("MyKey", "loadNativeAdHCPM2: calling impression")

                }

                override fun onAdClicked() {
                    super.onAdClicked()

                    trackAdEvents("Splash_Native_Clicked", "Admob_Native_Clicked")
                    inflateNextFreshNative()
                }


            })


            adLoader.build().loadAd(AdRequest.Builder().build())

            Log.i("MyKey", "loadNativeAdHCPM2: Ad requested")


        }

        /**
         * Medium CPM
         */

        fun loadNativeAdMCPM1(context: Context, adEvent: (Boolean) -> Unit) {

            if (nativeLoadingMCPM1 || isNativeAdLoadingNotPermissible(context)) {
                return
            }

            /**
             * this code will execute only if none of the above condition matches.
             */
            admobNativeRequestCounter++
            nativeLoadingMCPM1 = true
            val adLoader = getAdLoader(context, adData.nativeIdMCPM1) {
                nativeAdMCPM1 = it
            }


            adLoader.withAdListener(object : AdListener() {


                override fun onAdLoaded() {
                    super.onAdLoaded()
                    nativeLoadingMCPM1 = false
                    adEvent.invoke(true)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeLoadingMCPM1 = false
                    adEvent.invoke(false)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    if (nativeAdMCPM1 != null) {
                        tempNative = nativeAdMCPM1
                        nativeAdMCPM1 = null
                    }
                    loadNativeAdMCPM1(context) {}

                    trackAdEvents("Splash_Native_Impression", "Admob_Native_Impression")
                }

                override fun onAdClicked() {
                    super.onAdClicked()


                    trackAdEvents("Splash_Native_Clicked", "Admob_Native_Clicked")

                    inflateNextFreshNative()


                }
            })



            adLoader.build().loadAd(AdRequest.Builder().build())

        }

        fun loadNativeAdMCPM2(context: Context, adEvent: (Boolean) -> Unit) {
            if (nativeLoadingMCPM2 || isNativeAdLoadingNotPermissible(context))
                return

            /**
             * this code will execute only if none of the above condition matches.
             */
            admobNativeRequestCounter++
            nativeLoadingMCPM2 = true
            val adLoader = getAdLoader(context, adData.nativeIdMCPM2) {
                nativeAdMCPM2 = it
            }

            adLoader.withAdListener(object : AdListener() {


                override fun onAdLoaded() {
                    super.onAdLoaded()
                    nativeLoadingMCPM2 = false
                    adEvent.invoke(true)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeLoadingMCPM2 = false
                    adEvent.invoke(false)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    if (nativeAdMCPM2 != null) {
                        tempNative = nativeAdMCPM2
                        nativeAdMCPM2 = null
                    }
                    loadNativeAdMCPM2(context) {}

                    trackAdEvents("Splash_Native_Impression", "Admob_Native_Impression")

                }

                override fun onAdClicked() {
                    super.onAdClicked()

                    trackAdEvents("Splash_Native_Clicked", "Admob_Native_Clicked")

                    inflateNextFreshNative()

                }


            })

            adLoader.build().loadAd(AdRequest.Builder().build())

        }

        /**
         * Low CPM
         */
        fun loadNativeAdLCPM(context: Context, adEvent: (Boolean) -> Unit) {

            if (nativeLoadingLCPM || isNativeAdLoadingNotPermissible(context))
                return

            /**
             * this code will execute only if none of the above condition matches.
             */
            admobNativeRequestCounter++
            nativeLoadingLCPM = true
            val adLoader = getAdLoader(context, adData.nativeIdLCPM) {
                nativeAdLCPM = it
            }

            adLoader.withAdListener(object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    nativeLoadingLCPM = false
                    adEvent.invoke(true)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeLoadingLCPM = false
                    adEvent.invoke(false)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    if (nativeAdLCPM != null) {
                        tempNative = nativeAdLCPM
                        nativeAdLCPM = null
                    }
                    loadNativeAdLCPM(context) {}

                    trackAdEvents("Splash_Native_Impression", "Admob_Native_Impression")
                }

                override fun onAdClicked() {
                    super.onAdClicked()


                    trackAdEvents("Splash_Native_Clicked", "Admob_Native_Clicked")

                    inflateNextFreshNative()

                }


            })

            adLoader.build().loadAd(AdRequest.Builder().build())

        }


        fun showNative(
            context: Context,
            container: FrameLayout,
            callingPlaceName: String
        ) {
            Log.i("InflaterCallFromAdmob", "showNative: nativeInflater_Ran")
            if (nativeAdHCPM1 != null)
                inflateAdmobNativeAd(context, nativeAdHCPM1!!, container, callingPlaceName)
            else if (nativeAdMCPM1 != null) {
                inflateAdmobNativeAd(context, nativeAdMCPM1!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}
            } else if (nativeAdLCPM != null) {
                inflateAdmobNativeAd(context, nativeAdLCPM!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}
                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}
            } else if (tempNative != null) {
                inflateAdmobNativeAd(context, tempNative!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}
                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}
                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}
            } else {
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}
                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}
                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}
            }
        }


        fun showRVNative(
            context: Context,
            container: FrameLayout,
            callingPlaceName: String,
            adLoaded: (Boolean) -> Unit
        ) {
            if (nativeAdHCPM1 != null) {
                adLoaded.invoke(true)
                inflateAdmobNativeAd(context, nativeAdHCPM1!!, container, callingPlaceName)
            } else if (nativeAdHCPM2 != null) {
                adLoaded.invoke(true)
                inflateAdmobNativeAd(context, nativeAdHCPM2!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}
            } else if (nativeAdMCPM1 != null) {
                adLoaded.invoke(true)
                inflateAdmobNativeAd(context, nativeAdMCPM1!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}
            } else if (nativeAdMCPM2 != null) {
                adLoaded.invoke(true)
                inflateAdmobNativeAd(context, nativeAdMCPM2!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}
            } else if (nativeAdLCPM != null) {
                adLoaded.invoke(true)
                inflateAdmobNativeAd(context, nativeAdLCPM!!, container, callingPlaceName)


                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}
            } else if (tempNative != null) {
                adLoaded.invoke(true)
                inflateAdmobNativeAd(context, tempNative!!, container, callingPlaceName)


                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}


                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}


            } else {
                adLoaded.invoke(false)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}


                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}
            }
        }

        /**
         * call this method if you don't want to include no ads button in native ad container
         */

        fun showSmallNative(
            context: Context,
            container: FrameLayout,
            callingPlaceName: String,
            adLoaded: (Boolean) -> Unit
        ) {
            if (nativeAdHCPM1 != null) {
                adLoaded.invoke(true)
                inflateNativeAdWithNoAds(context, nativeAdHCPM1!!, container, callingPlaceName)
            } else if (nativeAdHCPM2 != null) {
                adLoaded.invoke(true)
                inflateNativeAdWithNoAds(context, nativeAdHCPM2!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}
            } else if (nativeAdMCPM1 != null) {
                adLoaded.invoke(true)
                inflateNativeAdWithNoAds(context, nativeAdMCPM1!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}
            } else if (nativeAdMCPM2 != null) {
                adLoaded.invoke(true)
                inflateNativeAdWithNoAds(context, nativeAdMCPM2!!, container, callingPlaceName)

                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}
            } else if (nativeAdLCPM != null) {
                adLoaded.invoke(true)
                inflateNativeAdWithNoAds(context, nativeAdLCPM!!, container, callingPlaceName)


                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}
            } else if (tempNative != null) {
                adLoaded.invoke(true)
                inflateNativeAdWithNoAds(context, tempNative!!, container, callingPlaceName)


                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}


                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}


            } else {
                adLoaded.invoke(false)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}


                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}
            }
        }

        fun showNativeWithoutNoAds(
            context: Context,
            container: FrameLayout,
            callingPlaceName: String
        ) {
            if (nativeAdHCPM1 != null)
                inflateNativeAdWithNoAds(context, nativeAdHCPM1!!, container, callingPlaceName)
            else if (nativeAdHCPM2 != null) {

                inflateNativeAdWithNoAds(context, nativeAdHCPM2!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

            } else if (nativeAdMCPM1 != null) {
                inflateNativeAdWithNoAds(context, nativeAdMCPM1!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}
            } else if (nativeAdMCPM2 != null) {
                inflateNativeAdWithNoAds(context, nativeAdMCPM2!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

            } else if (nativeAdLCPM != null) {
                inflateNativeAdWithNoAds(context, nativeAdLCPM!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}
            } else if (tempNative != null) {
                inflateNativeAdWithNoAds(context, tempNative!!, container, callingPlaceName)
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}

                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}
            } else {
                if (!nativeLoadingHCPM1)
                    loadNativeAdHCPM1(context) {}

                if (!nativeLoadingHCPM2)
                    loadNativeAdHCPM2(context) {}

                if (!nativeLoadingMCPM1)
                    loadNativeAdMCPM1(context) {}

                if (!nativeLoadingMCPM2)
                    loadNativeAdMCPM2(context) {}

                if (!nativeLoadingLCPM)
                    loadNativeAdLCPM(context) {}
            }
        }

        @SuppressLint("InflateParams")
        fun inflateNativeAdWithNoAds(
            context: Context,
            mNativeAd: NativeAd,
            container: FrameLayout,
            callingPlaceName: String
        ) {


            container.visibility = View.VISIBLE

            val layout = getLayoutType(callingPlaceName)


            val adView = LayoutInflater.from(context)
                .inflate(getLayout(layout), null) as NativeAdView


            // Inflate a layout and add it to the parent ViewGroup.
//            val adView = cardView.findViewById<NativeAdView>(R.id.ad_view)

            // Locate the view that will hold the headline, set its text, and use the
            // NativeAdView's headlineView property to register it.
            val headlineView = adView.findViewById<TextView>(R.id.my_ad_headline)
            headlineView.text = mNativeAd.headline
            adView.headlineView = headlineView

            if (layout != Layout.DIALOG) {
                val adBody = adView.findViewById<TextView>(R.id.my_ad_body)
                adBody.text = mNativeAd.body
                adView.bodyView = adBody
            }





            if (layout == Layout.DIALOG) {
                val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
                if (mNativeAd.icon != null) {
                    iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                    adView.iconView = iconView
                }
            } else if (layout == Layout.SIDE_MEDIA) {
                val mediaView = adView.findViewById<MediaView>(R.id.my_ad_media)
                if (adData.nativeScale != "center")
                    mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY)
                adView.mediaView = mediaView
            } else if (layout == Layout.SIDE_ICON) {
                val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
                if (mNativeAd.icon != null) {
                    iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                    adView.iconView = iconView
                }
            } else {
                val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
                if (mNativeAd.icon != null) {
                    iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                    adView.iconView = iconView
                }

                val mediaView = adView.findViewById<MediaView>(R.id.my_ad_media)
                if (adData.nativeScale != "center")
                    mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY)
                adView.mediaView = mediaView

            }


            val callToAction = adView.findViewById<Button>(R.id.my_ad_call_to_action_button)
            callToAction.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaColor, "#000000")
                )
            )
            callToAction.setTextColor(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaTextColor, "#ffffff")
                )
            )
            adView.callToActionView = callToAction


            // Call the NativeAdView's setNativeAd method to register the
            // NativeAdObject.
            adView.setNativeAd(mNativeAd)

            // Ensure that the parent view doesn't already contain an ad view.
            container.removeAllViews()

            // Place the AdView into the parent.
            container.addView(adView)
            container.visibility = View.VISIBLE


        }

        @SuppressLint("InflateParams")
        private fun inflateAdmobNativeAd(
            context: Context,
            mNativeAd: NativeAd,
            container: FrameLayout,
            callingPlaceName: String
        ) {

            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adLayout = LayoutNativeAdViewBinding.inflate(inflater)

            val layout = getLayoutType(callingPlaceName)

            // Inflate a layout and add it to the parent ViewGroup.
            val adView = LayoutInflater.from(context)
                .inflate(getLayout(layout), null) as NativeAdView

            if (adData.noAdsButton == "off")
                adLayout.noAds.visibility = View.GONE

            adLayout.noAds.setOnClickListener {
                trackAdEvents("No_Ads_Button_Clicked")
                BillingManager.showPremium(context) {}
            }


            // Locate the view that will hold the headline, set its text, and use the
            // NativeAdView's headlineView property to register it.
            val headlineView = adView.findViewById<TextView>(R.id.my_ad_headline)
            headlineView.text = mNativeAd.headline
            adView.headlineView = headlineView

            if (layout != Layout.DIALOG) {
                val adBody = adView.findViewById<TextView>(R.id.my_ad_body)
                adBody.text = mNativeAd.body
                adView.bodyView = adBody
            }





            if (layout == Layout.DIALOG) {
                val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
                if (mNativeAd.icon != null) {
                    iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                    adView.iconView = iconView
                }
            } else if (layout == Layout.SIDE_MEDIA) {
                val mediaView = adView.findViewById<MediaView>(R.id.my_ad_media)
                if (adData.nativeScale != "center")
                    mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY)
                adView.mediaView = mediaView
            } else if (layout == Layout.SIDE_ICON) {
                val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
                if (mNativeAd.icon != null) {
                    iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                    adView.iconView = iconView
                }
            } else {
                val iconView = adView.findViewById<ImageView>(R.id.my_ad_icon)
                if (mNativeAd.icon != null) {
                    iconView.setImageDrawable(mNativeAd.icon!!.drawable)
                    adView.iconView = iconView
                }

                val mediaView = adView.findViewById<MediaView>(R.id.my_ad_media)
                if (adData.nativeScale != "center")
                    mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY)
                adView.mediaView = mediaView
            }


            val callToAction = adView.findViewById<Button>(R.id.my_ad_call_to_action_button)
            callToAction.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaColor, "#000000")
                )
            )
            callToAction.setTextColor(
                Color.parseColor(
                    getColorValue(adData.AdmobCtaTextColor, "#ffffff")
                )
            )
            adView.callToActionView = callToAction


            // Call the NativeAdView's setNativeAd method to register the
            // NativeAdObject.
            adView.setNativeAd(mNativeAd)

            adLayout.nativeAdContainer.removeAllViews()
            adLayout.nativeAdContainer.addView(adView)

            // Ensure that the parent view doesn't already contain an ad view.
            container.removeAllViews()

            // Place the AdView into the parent.
            container.addView(adLayout.root)
            container.visibility = View.VISIBLE

        }


        /**
         * What conditions do we check here
         * if all of the ads are turned off
         * if Internet is not working
         * Check if user is premium or not
         * Check available requests we have
         */
        private fun isBannerAdLoadingNotPermissible(context: Context): Boolean {
            return adData.isOn.lowercase() == "off" || !isNetworkAvailable(context) || adData.inApp || adData.bannerRequests <= admobBannerRequestCounter
        }

        /**
         * Banner Ad
         */
        fun loadBannerAd1(activity: Activity, container: FrameLayout) {
            if (isBannerAdLoadingNotPermissible(activity)) {
                container.visibility = View.GONE
                return
            }

            /**
             * this code will only execute if above condition does not matches
             */
            val adView = AdView(activity)
            adView.adUnitId = adData.BannerId1
            adView.setAdSize(AdSize.BANNER)

            adView.adListener = object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    container.removeAllViews()
                    container.addView(adView)
                    container.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    loadBannerAd2(activity, container)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    trackAdEvents("Admob_Banner_Clicked")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    trackAdEvents("Admob_Banner_Impression")
                }

            }

            // Create an ad request. Check your logcat output for the hashed device ID to
            // get test ads on a physical device, e.g.,
            // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
            val adRequest = AdRequest
                .Builder()
                .build()

            // Start loading the ad in the background.
            adView.loadAd(adRequest)
            admobBannerRequestCounter += 1

        }

        fun loadBannerAd2(activity: Activity, container: FrameLayout) {
            if (isBannerAdLoadingNotPermissible(activity)) {
                container.visibility = View.GONE
                return
            }

            /**
             * this code will only execute if above condition does not matches
             */
            val adView = AdView(activity)
            adView.adUnitId = adData.BannerId2
            adView.setAdSize(AdSize.BANNER)

            adView.adListener = object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    container.removeAllViews()
                    container.addView(adView)
                    container.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    loadBannerAd3(activity, container)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    trackAdEvents("Admob_Banner_Clicked")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    trackAdEvents("Admob_Banner_Impression")
                }

            }

            // Create an ad request. Check your logcat output for the hashed device ID to
            // get test ads on a physical device, e.g.,
            // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
            val adRequest = AdRequest
                .Builder()
                .build()

            // Start loading the ad in the background.
            adView.loadAd(adRequest)
            admobBannerRequestCounter += 1

        }

        fun loadBannerAd3(activity: Activity, container: FrameLayout) {
            if (isBannerAdLoadingNotPermissible(activity)) {
                container.visibility = View.GONE
                return
            }

            /**
             * this code will only execute if above condition does not matches
             */
            val adView = AdView(activity)
            adView.adUnitId = adData.BannerId3
            adView.setAdSize(AdSize.BANNER)

            adView.adListener = object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    container.removeAllViews()
                    container.addView(adView)
                    container.visibility = View.VISIBLE
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    trackAdEvents("Admob_Banner_Clicked")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    trackAdEvents("Admob_Banner_Impression")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    loadBannerAd4(activity, container)
                }


            }

            // Create an ad request. Check your logcat output for the hashed device ID to
            // get test ads on a physical device, e.g.,
            // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
            val adRequest = AdRequest
                .Builder()
                .build()

            // Start loading the ad in the background.
            adView.loadAd(adRequest)
            admobBannerRequestCounter += 1

        }

        fun loadBannerAd4(activity: Activity, container: FrameLayout) {
            if (isBannerAdLoadingNotPermissible(activity)) {
                container.visibility = View.GONE
                return
            }

            /**
             * this code will only execute if above condition does not matches
             */
            val adView = AdView(activity)
            adView.adUnitId = AdsManager.adData.BannerId4
            adView.setAdSize(AdSize.BANNER)

            adView.adListener = object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    container.removeAllViews()
                    container.addView(adView)
                    container.visibility = View.VISIBLE
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    trackAdEvents("Admob_Banner_Clicked")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    trackAdEvents("Admob_Banner_Impression")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    loadBannerAd5(activity, container)
                }


            }

            // Create an ad request. Check your logcat output for the hashed device ID to
            // get test ads on a physical device, e.g.,
            // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
            val adRequest = AdRequest
                .Builder()
                .build()

            // Start loading the ad in the background.
            adView.loadAd(adRequest)
            admobBannerRequestCounter += 1

        }

        fun loadBannerAd5(activity: Activity, container: FrameLayout) {
            if (isBannerAdLoadingNotPermissible(activity)) {
                container.visibility = View.GONE
                return
            }

            /**
             * this code will only execute if above condition does not matches
             */
            val adView = AdView(activity)
            adView.adUnitId = adData.BannerId5
            adView.setAdSize(AdSize.BANNER)

            adView.adListener = object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    container.removeAllViews()
                    container.addView(adView)
                    container.visibility = View.VISIBLE
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    trackAdEvents("Admob_Banner_Clicked")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    trackAdEvents("Admob_Banner_Impression")
                }


            }

            // Create an ad request. Check your logcat output for the hashed device ID to
            // get test ads on a physical device, e.g.,
            // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
            val adRequest = AdRequest
                .Builder()
                .build()

            // Start loading the ad in the background.
            adView.loadAd(adRequest)
            admobBannerRequestCounter += 1

        }


        /**
         * Interstitial Ad
         */
        var interstitialAdHCPM1: InterstitialAd? = null
        var interstitialAdHCPM2: InterstitialAd? = null
        var interstitialAdMCPM1: InterstitialAd? = null
        var interstitialAdMCPM2: InterstitialAd? = null
        var interstitialAdLCPM: InterstitialAd? = null
        var interstitialAdLoadHCPM1 = false
        var interstitialAdLoadHCPM2 = false
        var interstitialAdLoadMCPM1 = false
        var interstitialAdLoadMCPM2 = false
        var interstitialAdLoadLCPM = false


        /**
         * What conditions do we check here
         * if all of the ads are turned off
         * if Internet is not working
         * Check if user is premium or not
         * Check available requests we have
         */
        private fun isInterstitialAdLoadingNotPermissible(context: Context): Boolean {
            return adData.isOn.lowercase() == "off" || !isNetworkAvailable(context) || adData.inApp || adData.interstitialRequests <= admobInterstitialRequestCounter
        }


        /**
         * High CPM
         */
        fun loadInterstitialAdHCPM1(context: Context, adLoaded: (Boolean) -> Unit) {
            if (interstitialAdLoadHCPM1 || isInterstitialAdLoadingNotPermissible(context))
                return

            /**
             * below code won't execute if each of the above condition matches
             */
            val adRequest = AdRequest.Builder().build()
            admobInterstitialRequestCounter += 1
            interstitialAdLoadHCPM1 = true
            InterstitialAd.load(
                context,
                adData.InterstitialIdHCPM1,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adLoaded.invoke(false)
                        interstitialAdHCPM1 = null
                        interstitialAdLoadHCPM1 = false

                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        adLoaded.invoke(true)
                        interstitialAdLoadHCPM1 = false
                        interstitialAdHCPM1 = interstitialAd
                    }
                })

        }

        fun loadInterstitialAdHCPM2(context: Context, adLoaded: (Boolean) -> Unit) {
            if (interstitialAdLoadHCPM2 || isInterstitialAdLoadingNotPermissible(context))
                return

            /**
             * below code won't execute if each of the above condition matches
             */
            val adRequest = AdRequest.Builder().build()
            admobInterstitialRequestCounter += 1
            interstitialAdLoadHCPM2 = true
            InterstitialAd.load(
                context,
                adData.InterstitialIdHCPM2,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adLoaded.invoke(false)
                        interstitialAdHCPM2 = null
                        interstitialAdLoadHCPM2 = false

                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        adLoaded.invoke(true)
                        interstitialAdLoadHCPM2 = false
                        interstitialAdHCPM2 = interstitialAd
                    }
                })

        }

        /**
         * Medium CPM
         */

        fun loadInterstitialAdMCPM1(context: Context, adLoaded: (Boolean) -> Unit) {
            if (interstitialAdLoadMCPM1 || isInterstitialAdLoadingNotPermissible(context))
                return

            /**
             * below code won't execute if each of the above condition matches
             */
            val adRequest = AdRequest.Builder().build()

            admobInterstitialRequestCounter += 1
            interstitialAdLoadMCPM1 = true
            InterstitialAd.load(
                context,
                adData.InterstitialIdMCPM1,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {

                        adLoaded.invoke(false)
                        interstitialAdMCPM1 = null
                        interstitialAdLoadMCPM1 = false
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        adLoaded.invoke(true)
                        interstitialAdLoadMCPM1 = false
                        interstitialAdMCPM1 = interstitialAd
                    }
                })

        }

        fun loadInterstitialAdMCPM2(context: Context, adLoaded: (Boolean) -> Unit) {
            if (interstitialAdLoadMCPM2 || isInterstitialAdLoadingNotPermissible(context))
                return

            /**
             * below code won't execute if each of the above condition matches
             */

            val adRequest = AdRequest.Builder().build()

            admobInterstitialRequestCounter += 1
            interstitialAdLoadMCPM2 = true
            InterstitialAd.load(
                context,
                adData.InterstitialIdMCPM2,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {

                        adLoaded.invoke(false)
                        interstitialAdMCPM2 = null
                        interstitialAdLoadMCPM2 = false
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        adLoaded.invoke(true)
                        interstitialAdLoadMCPM2 = false
                        interstitialAdMCPM2 = interstitialAd
                    }
                })

        }

        fun loadInterstitialAdLCPM(context: Context, adLoaded: (Boolean) -> Unit) {
            if (interstitialAdLoadLCPM || isInterstitialAdLoadingNotPermissible(context))
                return

            /**
             * below code won't execute if each of the above condition matches
             */
            val adRequest = AdRequest.Builder().build()

            admobInterstitialRequestCounter += 1
            interstitialAdLoadLCPM = true
            InterstitialAd.load(
                context,
                adData.InterstitialIdLCPM,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adLoaded.invoke(false)
                        interstitialAdLCPM = null
                        interstitialAdLoadLCPM = false
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        adLoaded.invoke(true)
                        interstitialAdLoadLCPM = false
                        interstitialAdLCPM = interstitialAd
                    }
                })

        }


        private var alertDialog: AlertDialog? = null


        fun showLoading(context: Context) {

            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adLoadingBinding = LayoutAdLoadingBinding.inflate(layoutInflater)

            Glide.with(context).load(R.drawable.loading_ad)
                .into(adLoadingBinding.loading)

            alertDialog = AlertDialog.Builder(context)
                .setView(adLoadingBinding.root)
                .setCancelable(false)
                .create()



            alertDialog?.show()
            alertDialog?.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        }

        fun dismissLoading() {
            alertDialog?.let {
                if (it.isShowing)
                    it.dismiss()
            }
        }

        private fun inflateInterstitialAd(
            activity: Activity,
            ad: InterstitialAd,
            adEvent: (Boolean) -> Unit
        ) {
            showLoading(activity)

            ad.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        adEvent.invoke(true)
                        AdsManager.isInterstitialDismissed = true
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        trackAdEvents("Admob_Interstitial_Clicked")
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        AdsManager.isInterstitialDismissed = false

                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        trackAdEvents("Admob_Interstitial_Impression")
                    }
                }

            Handler(Looper.getMainLooper()).postDelayed({

                dismissLoading()

                ad.show(activity)

            }, 1500)
        }

        fun showInterstitialAd(
            activity: Activity,
            adEvent: (Boolean) -> Unit
        ) {
            if (interstitialAdHCPM1 != null) {

                inflateInterstitialAd(activity, interstitialAdHCPM1!!) {
                    interstitialAdHCPM1 = null
                    adEvent.invoke(true)
                    loadInterstitialAdHCPM1(activity) {}
                }

            } else if (interstitialAdHCPM2 != null) {

                inflateInterstitialAd(activity, interstitialAdHCPM2!!) {
                    interstitialAdHCPM2 = null
                    adEvent.invoke(true)
                    loadInterstitialAdHCPM2(activity) {}
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

            } else if (interstitialAdMCPM1 != null) {

                inflateInterstitialAd(activity, interstitialAdMCPM1!!) {
                    interstitialAdMCPM1 = null
                    adEvent.invoke(true)
                    loadInterstitialAdMCPM1(activity) {}
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

            } else if (interstitialAdMCPM2 != null) {

                inflateInterstitialAd(activity, interstitialAdMCPM2!!) {
                    interstitialAdMCPM2 = null
                    adEvent.invoke(true)
                    loadInterstitialAdMCPM2(activity) {}
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

            } else if (interstitialAdLCPM != null) {

                inflateInterstitialAd(activity, interstitialAdLCPM!!) {
                    interstitialAdLCPM = null
                    adEvent.invoke(true)
                    loadInterstitialAdLCPM(activity) {}
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

                if (!interstitialAdLoadMCPM2)
                    loadInterstitialAdMCPM2(activity) {}

            } else {

                adEvent.invoke(false)

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

                if (!interstitialAdLoadMCPM2)
                    loadInterstitialAdMCPM2(activity) {}

                if (!interstitialAdLoadLCPM)
                    loadInterstitialAdLCPM(activity) {}

            }
        }


        /**
         * If we want to show interstitial without loading then this method will be used
         * ideal case is Splash screen where we show loading on splash and inflate interstitial ad on Dashboard
         */
        fun showInterstitialForSplash(
            activity: Activity,
            adEvent: (Boolean) -> Unit
        ) {
            if (interstitialAdHCPM1 != null) {

                inflateSplashInterstitialAd(activity, interstitialAdHCPM1!!) {
                    interstitialAdHCPM1 = null
                    adEvent.invoke(true)
                    loadInterstitialAdHCPM1(activity) {}
                }


            } else if (interstitialAdHCPM2 != null) {

                inflateSplashInterstitialAd(activity, interstitialAdHCPM2!!) {
                    interstitialAdHCPM2 = null
                    adEvent.invoke(true)
                    loadInterstitialAdHCPM2(activity) {}
                }


                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

            } else if (interstitialAdMCPM1 != null) {


                inflateSplashInterstitialAd(activity, interstitialAdMCPM1!!) {
                    interstitialAdMCPM1 = null
                    adEvent.invoke(true)
                    loadInterstitialAdMCPM1(activity) {}
                }



                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

            } else if (interstitialAdMCPM2 != null) {

                inflateSplashInterstitialAd(activity, interstitialAdMCPM2!!) {
                    interstitialAdMCPM2 = null
                    adEvent.invoke(true)
                    loadInterstitialAdMCPM2(activity) {}
                }


                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

            } else if (interstitialAdLCPM != null) {

                inflateSplashInterstitialAd(activity, interstitialAdLCPM!!) {
                    interstitialAdLCPM = null
                    adEvent.invoke(true)
                    loadInterstitialAdLCPM(activity) {}
                }



                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

                if (!interstitialAdLoadMCPM2)
                    loadInterstitialAdMCPM2(activity) {}

            } else {

                adEvent.invoke(false)
                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

                if (!interstitialAdLoadMCPM2)
                    loadInterstitialAdMCPM2(activity) {}

                if (!interstitialAdLoadLCPM)
                    loadInterstitialAdLCPM(activity) {}

            }
        }

        private fun inflateSplashInterstitialAd(
            activity: Activity,
            ad: InterstitialAd,
            adEvent: (Boolean) -> Unit
        ) {
            showLoading(activity)

            ad.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        adEvent.invoke(true)
                        AdsManager.isInterstitialDismissed = true

                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        AdsManager.isInterstitialDismissed = false
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        trackAdEvents(
                            "Admob_Splash_Interstitial_Clicked",
                            "Admob_Interstitial_Clicked"
                        )
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        trackAdEvents(
                            "Admob_Splash_Interstitial_Impression",
                            "Admob_Interstitial_Impression"
                        )
                    }
                }

            Handler(Looper.getMainLooper()).postDelayed({

                dismissLoading()

                ad.show(activity)

            }, 1500)
        }


        private fun inflateIntersAd(
            activity: Activity,
            ad: InterstitialAd,
            adEvent: (Boolean) -> Unit
        ) {
            showLoading(activity)

            ad.fullScreenContentCallback =
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        AdsManager.isInterstitialDismissed = true
                        adEvent.invoke(false)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        adEvent.invoke(true)
                        AdsManager.isInterstitialDismissed = false
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        trackAdEvents("Admob_Interstitial_Clicked")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        trackAdEvents("Admob_Interstitial_Impression")
                    }
                }

            Handler(Looper.getMainLooper()).postDelayed({

                dismissLoading()

                ad.show(activity)

            }, 1500)
        }

        /**
         * an extra method that returns callback when ad is shown
         */
        fun showIntersAd(
            activity: Activity,
            adEvent: (Boolean) -> Unit
        ) {
            if (interstitialAdHCPM1 != null) {

                inflateIntersAd(activity, interstitialAdHCPM1!!) {
                    if (it)
                        adEvent.invoke(true)
                    else {
                        interstitialAdHCPM1 = null
                        loadInterstitialAdHCPM1(activity) {}
                    }
                }

            } else if (interstitialAdHCPM2 != null) {

                inflateIntersAd(activity, interstitialAdHCPM2!!) {
                    if (it)
                        adEvent.invoke(true)
                    else {
                        interstitialAdHCPM2 = null
                        loadInterstitialAdHCPM2(activity) {}
                    }
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

            } else if (interstitialAdMCPM1 != null) {

                inflateIntersAd(activity, interstitialAdMCPM1!!) {
                    if (it)
                        adEvent.invoke(true)
                    else {
                        interstitialAdMCPM1 = null
                        loadInterstitialAdMCPM1(activity) {}
                    }
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

            } else if (interstitialAdMCPM2 != null) {

                inflateIntersAd(activity, interstitialAdMCPM2!!) {
                    if (it)
                        adEvent.invoke(true)
                    else {
                        interstitialAdMCPM2 = null
                        loadInterstitialAdMCPM2(activity) {}
                    }
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

            } else if (interstitialAdLCPM != null) {

                inflateIntersAd(activity, interstitialAdLCPM!!) {
                    if (it)
                        adEvent.invoke(true)
                    else {
                        interstitialAdLCPM = null
                        loadInterstitialAdLCPM(activity) {}
                    }
                }

                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

                if (!interstitialAdLoadMCPM2)
                    loadInterstitialAdMCPM2(activity) {}

            } else {

                adEvent.invoke(false)
                if (!interstitialAdLoadHCPM1)
                    loadInterstitialAdHCPM1(activity) {}

                if (!interstitialAdLoadHCPM2)
                    loadInterstitialAdHCPM2(activity) {}

                if (!interstitialAdLoadMCPM1)
                    loadInterstitialAdMCPM1(activity) {}

                if (!interstitialAdLoadMCPM2)
                    loadInterstitialAdMCPM2(activity) {}

                if (!interstitialAdLoadLCPM)
                    loadInterstitialAdLCPM(activity) {}

            }
        }

    }
}



