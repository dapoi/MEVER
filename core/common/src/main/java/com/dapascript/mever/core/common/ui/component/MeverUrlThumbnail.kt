package com.dapascript.mever.core.common.ui.component

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import coil3.compose.AsyncImage
import com.dapascript.mever.core.common.util.getPhotoThumbnail
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.getVideoThumbnail
import kotlinx.coroutines.delay

@Composable
fun MeverUrlThumbnail(
    source: String,
    modifier: Modifier = Modifier
) {
    val thumbnail = remember(source) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(thumbnail) {
        while (thumbnail.value == null) {
            try {
                thumbnail.value = if (getUrlContentType(source) == ".jpg") getPhotoThumbnail(source)
                else getVideoThumbnail(source)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (thumbnail.value == null) delay(2000)
        }
    }

    AnimatedContent(
        targetState = thumbnail.value,
        transitionSpec = { (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false)) },
        label = "Video Thumbnail Anim"
    ) { bitmap ->
        bitmap?.let {
            AsyncImage(
                model = it,
                contentScale = Crop,
                contentDescription = "Thumbnail",
                modifier = modifier
            )
        } ?: Box(modifier = modifier.background(meverShimmer()))
    }
}