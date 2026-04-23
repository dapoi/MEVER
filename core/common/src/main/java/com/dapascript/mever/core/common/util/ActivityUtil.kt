package com.dapascript.mever.core.common.util

import android.app.Activity
import android.content.Intent
import android.net.Uri.fromParts
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf
import com.dapascript.mever.core.common.util.DeviceType.PHONE

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { noLocalProvided() }
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