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
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.common.R

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

@Composable
fun getDescriptionPermission(permission: String) = when (permission) {
    READ_MEDIA_IMAGES -> stringResource(R.string.permission_request_read_media_image)
    READ_MEDIA_VIDEO -> stringResource(R.string.permission_request_read_media_video)
    READ_MEDIA_VISUAL_USER_SELECTED -> stringResource(R.string.permission_request_visual_selected)
    READ_EXTERNAL_STORAGE -> stringResource(R.string.permission_request_read_external)
    POST_NOTIFICATIONS -> stringResource(R.string.permission_request_notification)
    else -> stringResource(R.string.permission_request_write_external)
}