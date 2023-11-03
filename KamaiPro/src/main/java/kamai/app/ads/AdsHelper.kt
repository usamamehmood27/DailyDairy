package kamai.app.ads

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.FrameLayout
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import kamai.app.R
import kamai.app.enums.Layout
import kamai.app.interfaces.IEventListener
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * Created by Bilal Zurmati on 1st of april 2022
 *
 * Helper class to separate the functions
 */
class AdsHelper {
    companion object {
        var mCallingPlaceName: String? = null
        var adTypeWithAds = ""
        private var contextRef: WeakReference<Activity>? = null
        private var nativeContainer: WeakReference<FrameLayout>? = null
        lateinit var eventListener: IEventListener

        /**
         * Helper function to check if internet is connected
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // For API 29 or Above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                        ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            // For others API's below 29
            else {
                if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                    return true
                }
            }
            return false
        }

        fun initResources(
            activity: Activity,
            container: FrameLayout,
            adType: String,
            callingPlaceName: String
        ) {
            if (contextRef != null)
                contextRef = null
            contextRef = WeakReference(activity)

            if (mCallingPlaceName != null)
                mCallingPlaceName = null

            mCallingPlaceName = callingPlaceName
            adTypeWithAds = adType

            if (AdsManager.adData.reportClassName == "on")
                eventListener.onAdEvent(callingPlaceName)


            if (nativeContainer != null)
                nativeContainer = null



            nativeContainer = WeakReference(container)
        }

        /**
         * returns last context, ad was inflated with
         */
        fun getContext(): Activity? {
            return contextRef!!.get()
        }

        /**
         * returns last native layout container, ad was inflated with
         */
        fun getNativeContainer(): FrameLayout? {
            return nativeContainer!!.get()
        }

        /**
         * returns last Class instance, ad was inflated with
         */
        fun getLastNativeAdPlaceName(): String {
            return mCallingPlaceName!!
        }


        fun getColorValue(color: String, defaultColor: String): String {
            return if (color.length < 6)
                defaultColor
            else if (color.contains("#"))
                color
            else
                "#$color"
        }


        fun getLayoutType(callingPlaceName: String): Layout {

            if (AdsManager.adData.classesJson == "")
                return Layout.SIDE_MEDIA_WITH_ICON

            //getting layout for each Class name
            val jsonNative = JSONObject(AdsManager.adData.classesJson)
            val layoutType = if (jsonNative.has(callingPlaceName))
                jsonNative.getString(callingPlaceName)
            else
                "2"

            return when (layoutType) {
                "1" -> Layout.FULL
                "3" -> Layout.SIDE_MEDIA
                "4" -> Layout.SIDE_ICON
                "5" -> Layout.DIALOG
                else -> Layout.SIDE_MEDIA_WITH_ICON
            }
        }

        fun getLayout(layout: Layout): Int {
            return when (layout) {
                Layout.FULL -> R.layout.admob_native_bottom
                Layout.SIDE_MEDIA_WITH_ICON -> R.layout.admob_native_side_media
                Layout.SIDE_MEDIA -> R.layout.admob_native_no_icon
                Layout.SIDE_ICON -> R.layout.admob_native_with_icon
                Layout.DIALOG -> R.layout.admob_native_dialog
            }
        }

        /**
         * returns the layout for the Applovin
         */
        fun getBinder(templateId: Layout): MaxNativeAdViewBinder {
            val layoutId = if (templateId == Layout.FULL)
                R.layout.applovin_native_bottom_cta
            else
                R.layout.applovin_native_side_media

            return MaxNativeAdViewBinder.Builder(layoutId)
                .setTitleTextViewId(R.id.native_title)
                .setBodyTextViewId(R.id.native_text)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setIconImageViewId(R.id.native_icon_image)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setCallToActionButtonId(R.id.native_cta)
                .build()
        }

    }
}
