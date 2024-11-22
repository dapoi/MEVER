package com.dapascript.mever.core.common.ui.component

import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Offset.Companion.Zero
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.viewinterop.AndroidView
import coil3.load
import coil3.video.VideoFrameDecoder
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8

@Composable
fun MeverThumbnail(
    url: String,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(Dp8))
    ) {
        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { context -> ImageView(context).loadVideoUrl(url) { isLoading = it } },
            update = { imageView -> imageView.loadVideoUrl(url) { isLoading = it } }
        )

        if (isLoading) Box(
            modifier = modifier
                .matchParentSize()
                .background(shimmerBrush())
        )
    }
}

private fun ImageView.loadVideoUrl(url: String, onLoading: (Boolean) -> Unit) = this.apply {
    load(url) {
        decoderFactory { result, options, _ ->
            VideoFrameDecoder(result.source, options)
        }

        listener(
            onStart = { onLoading(true) },
            onSuccess = { _, _ -> onLoading(false) },
            onError = { _, _ -> onLoading(false) }
        )
    }
    scaleType = CENTER_CROP
}

@Composable
private fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    val color = Color(0xFF535454)
    return if (showShimmer) {
        val shimmerColors =
            listOf(
                color.copy(alpha = 0.6f),
                color.copy(alpha = 0.2f),
                color.copy(alpha = 0.6f),
            )
        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation =
            transition.animateFloat(
                initialValue = 0f,
                targetValue = targetValue,
                animationSpec =
                infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = Restart
                ),
                label = ""
            )
        linearGradient(
            colors = shimmerColors,
            start = Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        linearGradient(
            colors = listOf(Transparent, Transparent),
            start = Zero,
            end = Zero
        )
    }
}