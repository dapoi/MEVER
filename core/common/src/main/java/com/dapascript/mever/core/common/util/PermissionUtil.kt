package com.dapascript.mever.core.common.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi

val getStoragePermission = when {
    isAndroidUpSideDownCake() -> {
        arrayOf(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
    }

    isAndroidTiramisuAbove() -> {
        arrayOf(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES)
    }

    else -> arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
}

@RequiresApi(TIRAMISU)
val getNotificationPermission = POST_NOTIFICATIONS

@ChecksSdkIntAtLeast(api = TIRAMISU)
fun isAndroidTiramisuAbove() = SDK_INT >= TIRAMISU

@ChecksSdkIntAtLeast(api = UPSIDE_DOWN_CAKE)
fun isAndroidUpSideDownCake() = SDK_INT >= UPSIDE_DOWN_CAKE

fun getDescriptionPermission(permission: String) = when (permission) {
    READ_MEDIA_IMAGES -> "We need to access your images to download the file"
    READ_MEDIA_VIDEO -> "We need to access your videos to download the file"
    READ_MEDIA_VISUAL_USER_SELECTED -> "We need to access your visual user selected to download the file"
    READ_EXTERNAL_STORAGE -> "We need to access your storage to read the file"
    POST_NOTIFICATIONS -> "We need to access your notification to download the file"
    else -> "We need to access your storage to download the file"
}