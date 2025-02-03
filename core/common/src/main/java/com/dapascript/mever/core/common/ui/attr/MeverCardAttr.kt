package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.ketch.Status

object MeverCardAttr {
    data class MeverCardArgs(
        val source: String,
        val tag: String,
        val fileName: String,
        val status: Status,
        val progress: Int,
        val total: Long,
        val path: String,
        val icon: Int? = null,
        val iconBackgroundColor: Color? = null,
        val iconSize: Dp? = null,
        val iconPadding: Dp? = null,
        val urlThumbnail: String? = null
    )
}