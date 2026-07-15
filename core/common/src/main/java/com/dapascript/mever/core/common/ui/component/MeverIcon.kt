package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4

@Composable
fun MeverIcon(
    icon: Int,
    iconBackgroundColor: Color,
    iconSize: Dp,
    iconPadding: Dp,
    iconShadowColor: Color? = null
) {
    Box(
        modifier = Modifier
            .size(iconSize)
            .showShadow(shadowColor = iconShadowColor)
            .background(color = iconBackgroundColor, shape = CircleShape),
        contentAlignment = Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(iconPadding),
            painter = painterResource(icon),
            contentDescription = "Icon"
        )
    }
}

@Composable
fun Modifier.showShadow(shadowColor: Color? = null) = shadowColor?.let {
    this.shadow(elevation = Dp4, shape = CircleShape, ambientColor = it, spotColor = it)
} ?: this