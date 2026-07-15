package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.isDarkMode

fun Modifier.meverShimmer(
    showShimmer: Boolean = true,
    targetValue: Float = 1000f
): Modifier = composed {
    if (!showShimmer) return@composed this

    val color = if (isDarkMode) colors.whiteDarkGray else colors.lightGrayDarkGray
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
        label = "ShimmerAnimation"
    )

    drawWithCache {
        onDrawBehind {
            val animationValue = translateAnimation.value

            drawRect(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset.Zero,
                    end = Offset(x = animationValue, y = animationValue)
                )
            )
        }
    }
}