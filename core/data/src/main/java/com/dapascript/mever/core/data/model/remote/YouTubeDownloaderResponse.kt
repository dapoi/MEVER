package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeDownloaderResponse(
    val data: DataContent,
    val thumbnail: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val url: String? = null,
        val quality: String? = null
    )

    fun mapToEntity() = listOf(
        ContentEntity(
            url = data.url.orEmpty(),
            quality = data.quality.orEmpty(),
            thumbnail = thumbnail.orEmpty()
        )
    )
}
