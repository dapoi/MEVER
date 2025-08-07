package com.dapascript.mever.core.common.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi

val getStoragePermission = when {
    isAndroidUpSideDownCake() -> {
        listOf(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
    }

    isAndroidTiramisuAbove() -> {
        listOf(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES)
    }

    else -> listOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
}

@RequiresApi(TIRAMISU)
val getNotificationPermission = listOf(POST_NOTIFICATIONS)

@ChecksSdkIntAtLeast(api = TIRAMISU)
fun isAndroidTiramisuAbove() = SDK_INT >= TIRAMISU

@ChecksSdkIntAtLeast(api = UPSIDE_DOWN_CAKE)
fun isAndroidUpSideDownCake() = SDK_INT >= UPSIDE_DOWN_CAKE

@ChecksSdkIntAtLeast(api = Q)
fun isAndroidQAbove() = SDK_INT >= Q