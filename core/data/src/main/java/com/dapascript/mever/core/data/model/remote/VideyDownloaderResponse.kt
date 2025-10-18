package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideyDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val url: String? = null
    )

    fun mapToEntity() = listOf(
        ContentEntity(
            url = data?.url.orEmpty(),
            status = status ?: true,
            type = if (data?.url.orEmpty().endsWith("mp4")) "mp4" else "mp4"
        )
    )
}