package kamai.app.update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kamai.app.R
import kamai.app.ads.AdsManager.Companion.adData
import kamai.app.interfaces.IUpdateListener

/**
 * Created by BILAL ZURMATI on 19-July-2023
 */

class InAppUpdate {
    companion object {
        private lateinit var appUpdateManager: AppUpdateManager
        private lateinit var updateDownloadListener: IUpdateListener
        private var isFlexibleAppUpdateType: Boolean = false

        /**
         * Call this method in dashboard activity
         */
        fun init(context: Context) {
            if (!Companion::appUpdateManager.isInitialized)
                appUpdateManager = AppUpdateManagerFactory.create(context)
        }

        @SuppressLint("SwitchIntDef")
        private val updateListener: InstallStateUpdatedListener =
            InstallStateUpdatedListener { installState ->
                when (installState.installStatus()) {
                    InstallStatus.DOWNLOADED -> {
                        if (isFlexibleAppUpdateType && Companion::updateDownloadListener.isInitialized)
                            updateDownloadListener.updateDownloaded()
                    }
                }
            }


        /**
         * Call this method where you want to check app update, ideal place is dashboard activity
         * This method will check and show an update dialog if an update is available
         */
        fun checkAppUpdate(activity: Activity, updateRequired: (Boolean) -> Unit = {}) {
            Log.i("CheckUpdate", "checkAppUpdate: in check update")


            appUpdateManager.registerListener(updateListener)

            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

                if (appUpdateInfo.updateAvailability() != UpdateAvailability.UPDATE_AVAILABLE) {
                    Log.i("CheckUpdate", "checkAppUpdate: not available")

                    updateRequired.invoke(false)
                } else if (adData.appUpdateType == "2" && appUpdateInfo.isUpdateTypeAllowed(
                        AppUpdateType.IMMEDIATE
                    )
                ) {
                    Log.i("CheckUpdate", "checkAppUpdate: immediate")

                    isFlexibleAppUpdateType = false
                    startUpdateFlow(activity, appUpdateInfo, AppUpdateType.IMMEDIATE)
                    updateRequired.invoke(true)
                } else {
                    Log.i("CheckUpdate", "checkAppUpdate: Flexible")

                    isFlexibleAppUpdateType = true
                    startUpdateFlow(activity, appUpdateInfo, AppUpdateType.FLEXIBLE)
                    updateRequired.invoke(true)
                }

            }
        }

        private fun startUpdateFlow(
            activity: Activity,
            appUpdateInfo: AppUpdateInfo?,
            updateType: Int
        ) {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo!!, updateType, activity, 100)
        }

        /**
         * Need to call this method in every activity to register a listener for Installation of the update
         */
        fun registerListener(listener: IUpdateListener) {
            updateDownloadListener = listener
        }

        /**
         * This method will show a snack bar to user with an action button to install the downloaded updated.
         */
        fun showUserConsentDialog(context: Context, rootView: android.view.View) {
            Snackbar.make(
                rootView,
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction("RESTART") {
                    appUpdateManager.completeUpdate()
                }
                setActionTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
                show()
            }
        }
    }
}