package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun MeverActionButton(
    resource: Int,
    showBadge: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bellTransition by rememberInfiniteTransition(label = "Infinite Transition").animateFloat(
        initialValue = 25f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = LinearEasing
            ),
            repeatMode = Reverse
        ),
        label = "Rotation Animation"
    )

    Box(modifier = modifier.size(Dp32)) {
        IconButton(onClick = onDebounceClick { onClick() }) {
            Icon(
                modifier = Modifier
                    .size(Dp24)
                    .showGraphicLayer(state = showBadge, value = bellTransition),
                imageVector = ImageVector.vectorResource(resource),
                contentDescription = "Action Button"
            )
        }
        if (showBadge) Badge(
            modifier = Modifier
                .size(Dp15)
                .border(width = Dp2, color = colorScheme.background, shape = CircleShape)
                .align(TopEnd)
                .clip(CircleShape),
            containerColor = colorScheme.primary
        )
    }
}

private fun Modifier.showGraphicLayer(state: Boolean, value: Float) = if (state) {
    graphicsLayer(
        transformOrigin = TransformOrigin(
            pivotFractionX = 0.5f,
            pivotFractionY = 0.0f
        ),
        rotationZ = value
    )
} else this

@Composable
private fun onDebounceClick(
    debounceTimeMillis: Long = 1000L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTimeMillis: Long by remember { mutableLongStateOf(value = 0L) }
    return {
        System.currentTimeMillis().let { currentTimeMillis ->
            if ((currentTimeMillis - lastClickTimeMillis) >= debounceTimeMillis) {
                lastClickTimeMillis = currentTimeMillis
                onClick()
            }
        }
    }
}