package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.ui.theme.MeverTransparent

object MeverButtonAttr {
    sealed class MeverButtonType(
        open val backgroundColor: Color,
        open val contentColor: Color,
        open val borderColor: Color? = null
    ) {
        data class Filled(
            override val backgroundColor: Color,
            override val contentColor: Color
        ) : MeverButtonType(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )

        data class Outlined(
            override val contentColor: Color,
            override val borderColor: Color
        ) : MeverButtonType(
            backgroundColor = MeverTransparent,
            contentColor = contentColor,
            borderColor = borderColor
        )
    }
}