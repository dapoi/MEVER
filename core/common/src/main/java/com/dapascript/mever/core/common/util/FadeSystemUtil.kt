package com.dapascript.mever.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80

enum class FadeSide {
    Top, Bottom
}

private val scrimAlphaStops = listOf(
    1f, 0.963f, 0.913f, 0.851f, 0.776f, 0.690f, 0.595f, 0.495f,
    0.392f, 0.292f, 0.199f, 0.118f, 0.054f, 0.016f, 0.002f, 0f
)

@Composable
fun Modifier.fadingEdge(
    side: FadeSide,
    isVisible: Boolean,
    height: Dp = Dp80
): Modifier = if (isVisible) {
    this.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            val fadeHeightPx = height.toPx()
            val isTop = side == FadeSide.Top
            val fadeFraction = (fadeHeightPx / size.height).coerceAtMost(1f)

            val stops = mutableListOf<Pair<Float, Color>>()
            if (isTop) {
                scrimAlphaStops.asReversed().forEachIndexed { index, alpha ->
                    val fraction = (index.toFloat() / (scrimAlphaStops.size - 1)) * fadeFraction
                    stops.add(fraction to Color.Black.copy(alpha = alpha))
                }
                stops.add(1f to Color.Black)
            } else {
                stops.add(0f to Color.Black)
                scrimAlphaStops.forEachIndexed { index, alpha ->
                    val fraction =
                        (1f - fadeFraction) + (index.toFloat() / (scrimAlphaStops.size - 1)) * fadeFraction
                    stops.add(fraction to Color.Black.copy(alpha = alpha))
                }
            }

            drawRect(
                brush = Brush.verticalGradient(colorStops = stops.toTypedArray()),
                blendMode = BlendMode.DstIn
            )
        }
} else {
    this
}
