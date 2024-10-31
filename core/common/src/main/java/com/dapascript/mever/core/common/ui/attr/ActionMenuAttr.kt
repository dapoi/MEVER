package com.dapascript.mever.core.common.ui.attr

object ActionMenuAttr {

    data class ActionMenu(
        val resource: Int,
        val name: String,
        val isShowBadge: Boolean = false,
    )
}