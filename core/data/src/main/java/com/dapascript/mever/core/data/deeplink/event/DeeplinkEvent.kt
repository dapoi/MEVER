package com.dapascript.mever.core.data.deeplink.event

import com.dapascript.mever.core.data.model.local.ContentEntity

sealed interface DeeplinkEvent {

    data object Default : DeeplinkEvent

    data class DownloadResult(
        val url: String,
        val contents: List<ContentEntity> = emptyList(),
        val errorMessage: String = ""
    ) : DeeplinkEvent
}