package com.dapascript.mever.core.common.ui.attr

import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.ketch.Status

object MeverCardAttr {
    data class MeverCardArgs(
        val image: String,
        val title: String,
        val status: Status,
        val progress: Int,
        val total: Long,
        val type: PlatformType = UNKNOWN
    )
}