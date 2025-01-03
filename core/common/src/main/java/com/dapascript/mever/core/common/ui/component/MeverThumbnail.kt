package com.dapascript.mever.core.common.ui.component

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Offset.Companion.Zero
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import coil3.compose.AsyncImage
import com.dapascript.mever.core.common.ui.theme.MeverDarkGray
import com.dapascript.mever.core.common.util.getPhotoThumbnail
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.getVideoThumbnail
import kotlinx.coroutines.delay

@Composable
fun MeverThumbnail(
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
                contentDescription = null,
                contentScale = Crop,
                modifier = modifier
            )
        } ?: Box(modifier = modifier.background(shimmerBrush()))
    }
}

@Composable
private fun shimmerBrush(targetValue: Float = 1000f): Brush {
    val color = MeverDarkGray
    val shimmerColors = listOf(
        color.copy(alpha = 0.6f),
        color.copy(alpha = 0.2f),
        color.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "Infinite Transition")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = Restart
        ),
        label = "Shimmer Animation"
    )

    return linearGradient(
        colors = shimmerColors,
        start = Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}