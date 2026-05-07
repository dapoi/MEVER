package com.dapascript.mever.core.common.util

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE

class InAppUpdateManager(context: Context) {
    private val appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)

    fun startUpdate(
        updateAvailability: Int,
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        onUpdateNotAvailable: () -> Unit
    ) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == updateAvailability
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
            ) {
                try {
                    val updateOptions = AppUpdateOptions.newBuilder(IMMEDIATE)
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