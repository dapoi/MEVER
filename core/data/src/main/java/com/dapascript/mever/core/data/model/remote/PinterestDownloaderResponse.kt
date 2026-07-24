package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PinterestDownloaderResponse(
    val status: Boolean? = null,
    val data: List<DataContent>? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val url: String? = null,
        val thumbnail: String? = null
    )

    fun mapToEntity() = data?.mapIndexed { index, content ->
        ContentEntity(
            id = index.toString(),
            url = content.url.orEmpty(),
            status = status ?: true,
            thumbnail = content.thumbnail.orEmpty(),
            type = content.url?.substringAfterLast(".") ?: "jpg"
        )
    }
}