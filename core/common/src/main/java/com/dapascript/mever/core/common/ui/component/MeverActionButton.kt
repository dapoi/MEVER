package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverActionButton(
    resource: Int,
    showBadge: Boolean = false,
    onClick: () -> Unit
) {
    val deviceType = LocalDeviceType.current
    val animateIconVibrate = rememberInfiniteTransition(label = "Infinite Transition").animateFloat(
        initialValue = 25f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = FastOutLinearInEasing
            ),
            repeatMode = Reverse
        ),
        label = "Rotation Animation"
    )

    Box(
        modifier = Modifier
            .size(if (deviceType == PHONE) Dp32 else Dp40)
            .clip(RoundedCornerShape(Dp8))
            .onCustomClick { onClick() }
    ) {
        Icon(
            modifier = Modifier
                .size(if (deviceType == PHONE) Dp24 else Dp28)
                .showGraphicLayer(state = showBadge, value = { animateIconVibrate.value })
                .align(Center),
            painter = painterResource(id = resource),
            contentDescription = "Action Button"
        )
        if (showBadge) Badge(
            modifier = Modifier
                .size(Dp15)
                .border(width = Dp2, color = colors.whiteDark, shape = CircleShape)
                .align(TopEnd)
                .clip(CircleShape),
            containerColor = colors.alwaysPurple
        )
    }
}

private fun Modifier.showGraphicLayer(state: Boolean, value: () -> Float) = graphicsLayer {
    if (state) {
        transformOrigin = TransformOrigin(
            pivotFractionX = 0.5f,
            pivotFractionY = 0.0f
        )
        rotationZ = value()
    }
}