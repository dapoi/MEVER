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
        val trailingType: TrailingType
    ) {
        sealed class TrailingType {
            data class Default(
                val trailingTitle: String? = null,
                val trailingTitleColor: Color? = null
            ) : TrailingType()

            data class Switch(
                val switchState: Boolean = false
            ) : TrailingType()
        }
    }
}