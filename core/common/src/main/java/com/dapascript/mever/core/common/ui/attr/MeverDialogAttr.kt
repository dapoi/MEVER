package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color

object MeverDialogAttr {
    data class MeverDialogArgs(
        val title: String,
        val primaryButtonText: String,
        val titleColor: Color? = null,
        val backgroundColor: Color? = null,
        val dismissColor: Color? = null,
        val onClickAction: () -> Unit = {},
        val onDismiss: () -> Unit = {}
    )
}