package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class SoundCloudDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    internal data class DataContent(
        val url: String? = null,
        val title: String? = null,
        val thumbnail: String? = null
    )

    fun mapToEntity() = listOf(
        ContentEntity(
            status = status ?: true,
            url = data?.url.orEmpty(),
            fileName = data?.title?.let { "$it.mp3" }.orEmpty(),
            thumbnail = data?.thumbnail.orEmpty()
        )
    )
}