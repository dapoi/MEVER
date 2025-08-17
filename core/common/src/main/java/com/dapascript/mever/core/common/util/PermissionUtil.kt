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

fun getStoragePermission() = when {
    SDK_INT >= UPSIDE_DOWN_CAKE -> listOf(
        READ_MEDIA_VIDEO,
        READ_MEDIA_IMAGES,
        READ_MEDIA_VISUAL_USER_SELECTED
    )

    SDK_INT >= TIRAMISU -> listOf(
        READ_MEDIA_VIDEO,
        READ_MEDIA_IMAGES
    )

    else -> listOf(
        WRITE_EXTERNAL_STORAGE,
        READ_EXTERNAL_STORAGE
    )
}

fun getNotificationPermission() = if (SDK_INT >= TIRAMISU) {
    listOf(POST_NOTIFICATIONS)
} else emptyList()