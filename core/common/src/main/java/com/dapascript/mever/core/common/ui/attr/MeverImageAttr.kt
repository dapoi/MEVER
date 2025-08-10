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
import com.dapascript.mever.core.common.util.getUrlContentType
import kotlinx.coroutines.delay

object MeverImageAttr {
    @Composable
    fun getBitmapFromUrl(source: String, typeContent: String): Bitmap? {
        var resultExtracted by remember(source) { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(source, resultExtracted) {
            while (resultExtracted == null) {
                try {
                    resultExtracted = if (
                        getUrlContentType(
                            url = source,
                            responseType = typeContent
                        ).orEmpty().contains("jpg")
                    ) {
                        fetchPhotoFromUrl(source)
                    } else fetchVideoThumbnail(source)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (resultExtracted == null) delay(2000)
            }
        }
        return resultExtracted
    }
}