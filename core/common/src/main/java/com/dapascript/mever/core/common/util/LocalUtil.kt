package com.dapascript.mever.core.common.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri.fromParts
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf
import com.dapascript.mever.core.common.ui.theme.MeverColors
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import androidx.core.net.toUri

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { noLocalProvided() }
val LocalColors = staticCompositionLocalOf<MeverColors> { error("No colors provided") }
val LocalDeviceType = staticCompositionLocalOf { PHONE }

private fun noLocalProvided(): Nothing {
    error("CompositionLocal LocalActivity not present")
}

fun Activity.goToSetting() {
    Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Activity.goToDnsSetting() {
    Intent(Settings.ACTION_WIRELESS_SETTINGS).also(::startActivity)
}

fun Activity.goToWaStore(appPackageName: String = "com.whatsapp") {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "market://details?id=$appPackageName".toUri()
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    } catch (_: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$appPackageName".toUri()
            )
        )
    }
}