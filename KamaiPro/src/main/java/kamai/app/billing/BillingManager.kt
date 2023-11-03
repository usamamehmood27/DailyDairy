package kamai.app.billing

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClientStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kamai.app.R
import kamai.app.ads.AdsHelper
import kamai.app.ads.AdsHelper.Companion.eventListener
import kamai.app.ads.AdsHelper.Companion.isNetworkAvailable
import kamai.app.ads.AdsManager
import kamai.app.ads.AdsManager.Companion.adData
import kamai.app.databinding.LayoutPremiumBinding
import kamai.app.databinding.PrivacyPolycyBinding
import kamai.app.interfaces.IPurchaseListener
import kamai.app.models.AdData
import java.util.*


class BillingManager {
    companion object {

        lateinit var purchaseListener: IPurchaseListener

        /**
         * returns callback when dialog is closed
         */
        fun showPremium(context: Context, closed: (Boolean) -> Unit) {
            closed.invoke(false)
            return
            eventListener.onAdEvent("PremiumBanner_Show")
            var check = "y"
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bind = LayoutPremiumBinding.inflate(inflater)

            val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.setCancelable(false)
            dialog.setContentView(bind.root)

            Handler(Looper.getMainLooper()).postDelayed({
                bind.close.visibility = View.VISIBLE
            }, 3000)
            if (isNetworkAvailable(context)) {
                bind.t2.text = MonthlySku?.price
                bind.tt2.text = YearlySku?.price
                bind.ttt2.text = YearlySku?.price
            }
            if (isNetworkAvailable(context))
                bind.yearlyText.text =
                    StringBuilder("${YearlySku?.price} will be deducted while subscription. Subscription can be renewed automatically every year. You can cancel the subscription anytime from the Google Play Store.")

            bind.yearly.setOnClickListener {
                check = "y"
                bind.yearly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package)
                bind.monthly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
                bind.lifeTime.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
            }
            bind.monthly.setOnClickListener {
                check = "m"
                bind.monthly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package)
                bind.yearly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
                bind.lifeTime.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
            }
            bind.lifeTime.setOnClickListener {
                check = "l"
                bind.lifeTime.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package)
                bind.yearly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
                bind.monthly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
            }
            bind.privacyPolicy.setOnClickListener {
                showPrivacyDialog(
                    context,
                    "https://sites.google.com/view/new-pt-privacy-policay/home",
                    context.getString(R.string.privacy_policy)
                )
            }
            bind.termsAndCondition.setOnClickListener {
                showPrivacyDialog(
                    context,
                    "https://sites.google.com/view/new-pt-privacy-policay/home",
                    context.getString(R.string.terms_amp_condition)
                )
            }

            bind.close.setOnClickListener {
                closed.invoke(true)
                eventListener.onAdEvent("PremiumBanner_Close")
                dialog.dismiss()
            }




            bind.subscribe.setOnClickListener {

                if (isNetworkAvailable(context)) {
                    when (check) {
                        "y" -> {
                            YearlySku?.let {
                                launchPurchaseFlow(context as Activity, it)
                            }
                        }
                        "m" -> {
                            MonthlySku?.let {
                                launchPurchaseFlow(context as Activity, it)
                            }

                        }
                        "l" -> {
                            LifeTimeSku?.let {
                                launchPurchaseFlow(context as Activity, it)
                            }

                        }
                    }
                } else {
                    Toast.makeText(context, "You are Offline", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        }


        /**
         * returns callback when dialog is showed
         */
        fun showScreen(context: Context, closed: (Boolean) -> Unit) {
            closed.invoke(false)
            return
            eventListener.onAdEvent("PremiumBanner_Show")
            var check = "y"
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bind = LayoutPremiumBinding.inflate(inflater)

            val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.setCancelable(false)
            dialog.setContentView(bind.root)

            dialog.setOnShowListener {
                closed.invoke(true)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                bind.close.visibility = View.VISIBLE
            }, 3000)
            bind.t2.text = MonthlySku?.price
            bind.tt2.text = YearlySku?.price
            bind.ttt2.text = YearlySku?.price
            bind.yearlyText.text =
                StringBuilder("${YearlySku?.price} will be deducted while subscription. Subscription can be renewed automatically every year. You can cancel the subscription anytime from the Google Play Store.")

            bind.yearly.setOnClickListener {
                check = "y"
                bind.yearly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package)
                bind.monthly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
                bind.lifeTime.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
            }
            bind.monthly.setOnClickListener {
                check = "m"
                bind.monthly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package)
                bind.yearly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
                bind.lifeTime.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
            }
            bind.lifeTime.setOnClickListener {
                check = "l"
                bind.lifeTime.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package)
                bind.yearly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
                bind.monthly.background =
                    ContextCompat.getDrawable(context, R.drawable.premium_select_package_with_color)
            }
            bind.privacyPolicy.setOnClickListener {
                showPrivacyDialog(
                    context,
                    "https://sites.google.com/view/new-pt-privacy-policay/home",
                    context.getString(R.string.privacy_policy)
                )
            }
            bind.termsAndCondition.setOnClickListener {
                showPrivacyDialog(
                    context,
                    "https://sites.google.com/view/new-pt-terms-condition/home",
                    context.getString(R.string.terms_amp_condition)
                )
            }

            bind.close.setOnClickListener {
                eventListener.onAdEvent("PremiumBanner_Close")
                dialog.dismiss()
            }

            bind.subscribe.setOnClickListener {

                if (isNetworkAvailable(context)) {
                    when (check) {
                        "y" -> {
                            YearlySku?.let {
                                launchPurchaseFlow(context as Activity, it)
                            }
                        }
                        "m" -> {
                            MonthlySku?.let {
                                launchPurchaseFlow(context as Activity, it)
                            }
                        }
                        "l" -> {
                            LifeTimeSku?.let {
                                launchPurchaseFlow(context as Activity, it)
                            }

                        }
                    }
                } else {
                    Toast.makeText(context, "You are Offline", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        }


        var LifeTimeSku: SkuDetails? = null
        var MonthlySku: SkuDetails? = null
        var YearlySku: SkuDetails? = null

//        const val LIFE_TIME_PRODUCT = "new_pth_lifetime_offer"
//        const val MONTHLY_PRODUCT = "new_pth_monthly_offer"
//        const val YEARLY_PRODUCT = "new_pth_yearly_offer"

        const val LIFE_TIME_PRODUCT = "ct_lifetime_purchase"
        const val MONTHLY_PRODUCT = "ct_monthly_subscription"
        const val YEARLY_PRODUCT = "ct_yearly_subscription"



//        To handle these situations, be sure that your app calls BillingClient.queryPurchasesAsync() in your onResume() and onCreate() methods to
//        ensure that all purchases are successfully processed as described in processing

        lateinit var billingClient: BillingClient

        fun init(context: Context, initialized: (Boolean) -> Unit) {
            billingClient = BillingClient.newBuilder(context)
                .setListener { billingResult, purchases ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
                        for (purchase in purchases) {
                            runBlocking {
                                handlePurchase(purchase)
                            }
                        }
                    } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                        // Handle an error caused by a user cancelling the purchase flow.
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle any other error codes.
                        Log.i("MyError", "init: ${billingResult.responseCode}")
                    }
                }
                .enablePendingPurchases()
                .build()

            //initialize billing client
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    initialized.invoke(false)
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    initialized.invoke(true)
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        runBlocking { queryProductDetails() }
                    }
                }

            })
        }

        fun launchPurchaseFlow(activity: Activity, skuDetails: SkuDetails) {
            val flowParams = skuDetails.let {
                BillingFlowParams.newBuilder()
                    .setSkuDetails(it)
                    .build()
            }
            flowParams.let {
                billingClient.launchBillingFlow(
                    activity,
                    it
                ).responseCode
            }
        }


        suspend fun queryProductDetails() {

            val skuListLifeTime = ArrayList<String>()
            skuListLifeTime.add(LIFE_TIME_PRODUCT)
            val paramsLifeTime = SkuDetailsParams.newBuilder()
                .setSkusList(skuListLifeTime)
                .setType(BillingClient.SkuType.INAPP)

            val skuListSubs = ArrayList<String>()
            skuListSubs.add(MONTHLY_PRODUCT)
            skuListSubs.add(YEARLY_PRODUCT)
            val paramsSubs = SkuDetailsParams.newBuilder()
                .setSkusList(skuListSubs)
                .setType(BillingClient.SkuType.SUBS)


            // leverage querySkuDetails Kotlin extension function
            withContext(Dispatchers.IO) {
                billingClient.querySkuDetailsAsync(
                    paramsLifeTime.build()
                ) { _, skuList ->
                    if (!skuList.isNullOrEmpty())
                        LifeTimeSku = skuList[0]
                    else
                        Log.i("MySku", "querySkuDetails: empty")
                }

                billingClient.querySkuDetailsAsync(paramsSubs.build()) { _, skuList ->
                    skuList?.let {
                        for (sku in it) {
                            if (sku.sku == MONTHLY_PRODUCT)
                                MonthlySku = sku
                            else
                                YearlySku = sku
                            Log.i("MySkus", "querySkuDetails: ${sku.sku}")
                        }
                    }
                }

            }

        }


        private fun querySubscriptions(adData: AdData) {
            billingClient.queryPurchasesAsync(
                BillingClient.SkuType.SUBS
            ) { result, records ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && records.isNotEmpty()) {
                    var isPremium = false
                    for (record in records) {
                        Log.i("MyPurchases", "queryPurchases: ${record.originalJson}")
                        if (record.skus.contains(YEARLY_PRODUCT)) {
                            if (checkValidity(record.purchaseTime, 360)) {
                                if (::purchaseListener.isInitialized && !adData.inApp)
                                    purchaseListener.activatePremiumVersion()
                                isPremium = true
                                break
                            }
                        } else if (record.skus.contains(MONTHLY_PRODUCT)) {
                            if (checkValidity(record.purchaseTime, 30)) {
                                if (::purchaseListener.isInitialized && !adData.inApp)
                                    purchaseListener.activatePremiumVersion()
                                isPremium = true
                                break
                            }
                        }

                    }
//                    <----Update your shared preference value here---->
//                    PowerPreference.getDefaultFile().putBoolean(is_Subscribed, isPremium)
                } else {
//                <----Update your shared preference value here---->
//                    PowerPreference.getDefaultFile().putBoolean(
//                        is_Subscribed,
//                        false
//                    )  //This may require second open to apply changes
                }


            }
        }

        fun queryPurchases(adData: AdData) {
            billingClient.queryPurchaseHistoryAsync(
                BillingClient.SkuType.INAPP
            ) { result, records ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && !records.isNullOrEmpty() && records[0].skus.contains(
                        LIFE_TIME_PRODUCT
                    )
                ) {
                    if (::purchaseListener.isInitialized && !adData.inApp)
                        purchaseListener.activatePremiumVersion()
                } else
                    querySubscriptions(adData)

            }
        }


        private fun checkValidity(purchaseTime: Long, duration: Int): Boolean {

            val difference: Long = System.currentTimeMillis() - purchaseTime
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()
            val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
//            min =
//                (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) as Int / (1000 * 60)
//            hours = if (hours < 0) -hours else hours
//            Log.i("======= Hours", " :: $hours")
            Log.i("MyDays", "checkValidity: $hours")
            return days < duration
        }

        private suspend fun handlePurchase(purchase: Purchase) {
            Log.i("MyPurchase", "handlePurchase:Order id ${purchase.orderId}")
            Log.i("MyPurchase", "handlePurchase:Package name ${purchase.packageName}")

            if ((purchase.skus.contains(LIFE_TIME_PRODUCT.lowercase()) || purchase.skus.contains(
                    YEARLY_PRODUCT.lowercase()
                ) || purchase.skus.contains(MONTHLY_PRODUCT.lowercase()))
                && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            ) {
                //<-------update your boolean value stored in shared preferences here--------->
//                PreferenceHelper.putBoolean(Constants.INAPP, true)
                if (!purchase.isAcknowledged) {

                    Log.i("MyKey", "handlePurchase: Acknowleding purchase")
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                    withContext(Dispatchers.IO) {
                        billingClient.acknowledgePurchase(
                            acknowledgePurchaseParams.build()
                        ) { result ->
                            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                Log.i("MyKey", "handlePurchase: Dialog shown")
                                if (::purchaseListener.isInitialized)
                                    purchaseListener.activatePremiumVersion()

                            }
                        }
                    }


                } else {
                    Log.i("MyKey", "handlePurchase: Purchase is already acknowledged")
                    if (::purchaseListener.isInitialized)
                        purchaseListener.activatePremiumVersion()
                }
            } else if ((purchase.skus.contains(YEARLY_PRODUCT.lowercase()) || purchase.skus.contains(
                    MONTHLY_PRODUCT.lowercase()
                ))
                && purchase.purchaseState == Purchase.PurchaseState.PENDING
            ) {
                Log.i("MyKey", "handlePurchase: Pending")

            } else {
                Log.i("MyKey", "handlePurchase: No purchase yet")

            }


            // Verify the purchase.
            // Ensure entitlement was not already granted for this purchaseToken.
            // Grant entitlement to the user.

            val consumeParams =
                ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            val consumeResult = withContext(Dispatchers.IO) {
                billingClient.consumeAsync(
                    consumeParams
                ) { p0, p1 -> }
            }
        }

        //        <--- Wrap calling of this method around below line of code -->
//        if (activity.window.decorView.rootView.isShown)
        fun showProgressDialog(context: Context, intent: Intent) {
            //<-------update your boolean value stored in shared preferences here--------->
//            PreferenceHelper.putBoolean(Constants.INAPP, true)
            Log.i("MyPurchase", "showProgressDialog: in progress dialog")

            Handler(Looper.getMainLooper()).post {
                val myView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_restart_dialog, null)
                val alertDialog = AlertDialog.Builder(context)
                    .setView(myView)
                    .setCancelable(false)
                    .create()


                myView.findViewById<Button>(R.id.btn_ok)
                    .setOnClickListener {
                        alertDialog.dismiss()
                        AdsManager.clearInstances()
                        (context as Activity).finishAffinity()
                        (context as Activity).startActivity(intent)
                    }

                alertDialog.show()
                alertDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
                Log.i("MyPurchase", "querySkuDetails: showed")
            }

        }

        fun showPrivacyDialog(context: Context, myUrl: String, heading: String) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bind = PrivacyPolycyBinding.inflate(inflater)
            val dialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(bind.root)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//            bind.dialogHeading.text = heading
//            bind.webviewPrivacy.loadUrl(myUrl)

//            bind.webviewPrivacy.webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    bind.webviewPrivacy.visibility = View.VISIBLE
//                    bind.progressWebview.visibility = View.GONE
//                }
//            }

            dialog.findViewById<ImageView>(R.id.dialogBack).setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            dialog.show()
        }
    }
}
