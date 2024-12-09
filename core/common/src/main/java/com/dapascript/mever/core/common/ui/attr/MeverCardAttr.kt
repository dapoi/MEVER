package com.dapascript.mever.core.common.ui.attr

import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADING
import com.ketch.Status

object MeverCardAttr {
    data class MeverCardArgs(
        val image: String,
        val tag: String,
        val metaData: String,
        val fileName: String,
        val status: Status,
        val progress: Int,
        val total: Long,
        val path: String,
        val type: MeverCardType = DOWNLOADING,
        val onPlayClick: (() -> Unit)? = null,
        val onDownloadingClick: (() -> Unit)? = null,
        val onShareContentClick: (() -> Unit)? = null,
        val onDeleteContentClick: (() -> Unit)? = null
    )

    enum class MeverCardType {
        DOWNLOADING,
        DOWNLOADED
    }
}