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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun MeverIcon(
    icon: Int,
    iconBackgroundColor: Color,
    iconSize: Dp,
    iconPadding: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = iconBackgroundColor, shape = CircleShape)
            .size(iconSize),
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