package com.dapascript.mever.core.common.ui.attr

import com.ketch.Status

object MeverCardAttr {
    data class MeverCardArgs(
        val image: String,
        val fileName: String,
        val status: Status,
        val progress: Int,
        val total: Long,
        val path: String
    )
}