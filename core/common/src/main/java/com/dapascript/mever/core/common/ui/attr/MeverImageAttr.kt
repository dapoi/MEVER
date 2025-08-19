package com.dapascript.mever.core.common.ui.attr

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.util.fetchPhotoFromUrl
import com.dapascript.mever.core.common.util.fetchVideoThumbnail
import com.dapascript.mever.core.common.util.getExtensionFromUrl
import kotlinx.coroutines.delay

object MeverImageAttr {
    @Composable
    fun getBitmapFromUrl(url: String, extensionFile: String): Bitmap? {
        var resultExtracted by remember(url) { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(url, resultExtracted) {
            while (resultExtracted == null) {
                try {
                    resultExtracted = if (
                        getExtensionFromUrl(
                            url = url,
                            extensionFile = extensionFile
                        ).orEmpty().contains("jpg")
                    ) {
                        fetchPhotoFromUrl(url)
                    } else fetchVideoThumbnail(url)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (resultExtracted == null) delay(2000)
            }
        }
        return resultExtracted
    }
}