package com.dapascript.mever.core.common.util

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions

class InAppUpdateManager(context: Context) {
    private val appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)

    fun startUpdate(
        updateType: Int,
        updateAvailability: Int,
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        onUpdateNotAvailable: () -> Unit
    ) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == updateAvailability
                && appUpdateInfo.isUpdateTypeAllowed(updateType)
            ) {
                try {
                    val updateOptions = AppUpdateOptions.newBuilder(updateType)
                        .setAllowAssetPackDeletion(true)
                        .build()

                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        launcher,
                        updateOptions
                    )
                } catch (_: Exception) {
                    onUpdateNotAvailable()
                }
            } else {
                onUpdateNotAvailable()
            }
        }.addOnFailureListener {
            onUpdateNotAvailable()
        }
    }
}