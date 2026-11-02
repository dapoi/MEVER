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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors

@Composable
fun MeverIcon(
    icon: Int,
    iconBackgroundColor: Color,
    iconSize: Dp,
    iconPadding: Dp,
    iconShadowColor: Color? = null,
    iconTint: Color? = null
) {
    val isDoodle = MeverThemeAttr.isDoodle
    val effectiveBg = if (isDoodle) {
        if (iconBackgroundColor == colors.whiteDark || iconBackgroundColor == colors.whiteBlack) iconBackgroundColor
        else colors.lightPurpleDarkGray
    } else iconBackgroundColor

    val effectiveColorFilter = when {
        isDoodle && iconTint != null -> tint(colors.blackWhite)
        isDoodle -> ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
        iconTint != null -> tint(iconTint)
        else -> null
    }

    Box(
        modifier = Modifier
            .size(iconSize)
            .showShadow(shadowColor = if (isDoodle) null else iconShadowColor)
            .background(color = effectiveBg, shape = CircleShape),
        contentAlignment = Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(iconPadding),
            painter = painterResource(icon),
            colorFilter = effectiveColorFilter,
            contentDescription = "Icon"
        )
    }
}

@Composable
fun Modifier.showShadow(shadowColor: Color? = null) = shadowColor?.let {
    this.shadow(elevation = Dp4, shape = CircleShape, ambientColor = it, spotColor = it)
} ?: this