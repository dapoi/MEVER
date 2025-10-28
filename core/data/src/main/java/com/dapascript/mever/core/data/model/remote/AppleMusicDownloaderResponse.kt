package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppleMusicDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val thumbnail: String? = null,
        val audio: Audio? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Audio(
            val filename: String? = null,
            val url: String? = null
        )
    }

    fun mapToEntity() = listOf(
        ContentEntity(
            url = data?.audio?.url.orEmpty(),
            status = status ?: true,
            thumbnail = data?.thumbnail.orEmpty(),
            fileName = data?.audio?.filename.orEmpty()
        )
    )
}