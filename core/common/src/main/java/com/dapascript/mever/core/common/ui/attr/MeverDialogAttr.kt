package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color

object MeverDialogAttr {
    data class MeverDialogArgs(
        val title: String,
        val primaryButtonText: String? = null,
        val secondaryButtonText: String? = null,
        val titleColor: Color? = null,
        val backgroundColor: Color? = null,
        val primaryButtonColor: Color? = null,
        val secondaryButtonColor: Color? = null,
        val onClickPrimaryButton: () -> Unit = {},
        val onClickSecondaryButton: () -> Unit = {}
    )
}