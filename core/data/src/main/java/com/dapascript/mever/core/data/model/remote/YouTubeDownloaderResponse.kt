package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.common.util.sanitizeFilename
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null,
    val thumbnail: String? = null,
    val msg: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val url: String? = null,
        val filename: String? = null,
        val quality: String? = null,
        val extension: String? = null
    )

    fun mapToEntity() = data?.let {
        listOf(
            ContentEntity(
                url = data.url.orEmpty(),
                status = status ?: true,
                fileName = sanitizeFilename(data.filename.orEmpty()),
                quality = data.quality.orEmpty(),
                type = data.extension.orEmpty(),
                thumbnail = thumbnail.orEmpty()
            )
        )
    } ?: listOf(
        ContentEntity(
            url = "",
            status = false,
            message = msg.orEmpty()
        )
    )
}