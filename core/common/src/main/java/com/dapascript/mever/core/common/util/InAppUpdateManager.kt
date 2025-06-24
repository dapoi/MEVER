package com.dapascript.mever.core.common.util

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE

class InAppUpdateManager(context: Context) {
    private val appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)
    private val appUpdateInfoTask = appUpdateManager.appUpdateInfo

    fun startUpdate(
        updateAvailability: Int,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) = try {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == updateAvailability
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
            ) {
                val updateOptions = AppUpdateOptions.newBuilder(IMMEDIATE)
                    .setAllowAssetPackDeletion(true)
                    .build()
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    launcher,
                    updateOptions
                )
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}