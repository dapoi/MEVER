package com.dapascript.mever.core.common.ui.attr

object MeverDialogAttr {
    data class MeverDialogArgs(
        val title: String,
        val primaryButtonText: String,
        val onPrimaryButtonClick: () -> Unit,
        val onSecondaryButtonClick: () -> Unit
    )
}