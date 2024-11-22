package com.dapascript.mever.core.common.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi

val getStoragePermission = if (isAndroidTiramisuAbove()) {
    arrayOf(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES)
} else arrayOf(WRITE_EXTERNAL_STORAGE)

@RequiresApi(TIRAMISU)
val getNotificationPermission = POST_NOTIFICATIONS

@ChecksSdkIntAtLeast(api = TIRAMISU)
fun isAndroidTiramisuAbove() = SDK_INT >= TIRAMISU

fun getDescriptionPermission(permission: String) = when (permission) {
    READ_MEDIA_IMAGES -> "We need to access your images to download the file"
    READ_MEDIA_VIDEO -> "We need to access your videos to download the file"
    else -> "We need to access your storage to download the file"
}