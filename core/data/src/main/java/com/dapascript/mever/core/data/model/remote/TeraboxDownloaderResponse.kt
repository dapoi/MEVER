package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeraboxDownloaderResponse(
    val data: List<DataContent>? = null
) {
    data class DataContent(
        val thumbs: Thumbs? = null,
        val url: String? = null
    ) {
        data class Thumbs(
            @Json(name = "url1") val thumbnail: String? = null
        )
    }

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            thumbnail = it.thumbs?.thumbnail.orEmpty()
        )
    }
}