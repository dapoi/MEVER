package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADING
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.UNKNOWN
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.ketch.Status
import com.ketch.Status.*

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
        val iconSize: Dp = Dp48,
        val iconPadding: Dp = Dp10,
        val onClickPlay: (() -> Unit)? = null,
        val onClickDownloading: (() -> Unit)? = null,
        val onClickShare: (() -> Unit)? = null,
        val onClickDelete: (() -> Unit)? = null
    )

    enum class MeverCardType {
        DOWNLOADING,
        DOWNLOADED,
        UNKNOWN
    }

    fun getCardType(status: Status) = when (status) {
        QUEUED, STARTED, PAUSED, PROGRESS -> DOWNLOADING
        SUCCESS -> DOWNLOADED
        CANCELLED -> UNKNOWN
        FAILED -> UNKNOWN
        DEFAULT -> UNKNOWN
    }
}