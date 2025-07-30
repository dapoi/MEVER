package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TwitterDownloaderResponse(
    val data: List<DataContent>? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val type: String? = null,
        val url: String? = null
    )

    fun mapToEntity() = when (data?.firstOrNull()?.type?.lowercase()) {
        "mp4" -> data.take(1)
        "jpg" -> data
        else -> emptyList()
    }.map {
        ContentEntity(url = it.url.orEmpty(), type = it.type.orEmpty())
    }
}
