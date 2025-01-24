package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
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
        val iconSize: Dp = Dp24,
        val iconPadding: Dp = Dp5,
        val urlThumbnail: String = ""
    )
}