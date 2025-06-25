package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Offset.Companion.Zero
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import com.dapascript.mever.core.common.ui.theme.MeverTransparent

@Composable
fun meverShimmer(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    val color = colorScheme.onSurface

    return if (showShimmer) {
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

        linearGradient(
            colors = shimmerColors,
            start = Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else linearGradient(
        colors = listOf(MeverTransparent, MeverTransparent),
        start = Zero,
        end = Zero
    )
}