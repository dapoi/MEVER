package com.dapascript.mever.core.data.deeplink.event

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity

sealed interface DeeplinkEvent {

    data object Default : DeeplinkEvent

    data class SharedUrl(val url: String) : DeeplinkEvent

    data class DownloadResult(
        val url: String,
        val contents: List<ContentEntity>,
        val errorMessage: String
    ) : DeeplinkEvent

    data class ImageGenerator(
        val prompt: String,
        val artStyle: String,
        val imageResponse: String,
        val errorMessage: String
    ) : DeeplinkEvent
}