package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

object MeverMenuItemAttr {
    data class MenuItemArgs(
        val leadingTitle: String,
        val leadingIcon: Int,
        val leadingIconBackground: Color,
        val leadingIconSize: Dp,
        val leadingIconPadding: Dp,
        val trailingTitle: String? = null
    )
}