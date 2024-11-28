package com.dapascript.mever.core.common.ui.attr

object MeverDialogAttr {
    data class MeverDialogArgs(
        val title: String,
        val actionText: String? = null,
        val onActionClick: (() -> Unit)? = null,
        val onDismissClick: (() -> Unit)? = null
    )
}