package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.unit.Dp

object MeverTabsAttr {
    enum class SubComposeID {
        PRE_CALCULATE_ITEM,
        ITEM,
        INDICATOR
    }

    data class TabPosition(
        val left: Dp, val width: Dp
    )
}