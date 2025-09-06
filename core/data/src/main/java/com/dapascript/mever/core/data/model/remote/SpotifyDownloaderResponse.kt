package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpotifyDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val title: String? = null,
        val thumbnail: String? = null,
        val url: String? = null
    )

    fun mapToEntity() = listOf(
        ContentEntity(
            fileName = data?.title?.let { "$it.mp3" }.orEmpty(),
            url = data?.url.orEmpty(),
            status = status ?: true,
            thumbnail = data?.thumbnail.orEmpty(),
            type = "mp3"
        )
    )
}