package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class TeraboxDownloaderResponse(
    val status: Boolean? = null,
    val data: List<DataContent>? = null
) {
    internal data class DataContent(
        val thumbs: Thumbs? = null,
        val url: String? = null,
        @Json(name = "server_filename") val serverFileName: String? = null
    ) {
        internal data class Thumbs(
            @Json(name = "url1") val thumbnail: String? = null
        )
    }

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            status = status ?: true,
            fileName = it.serverFileName.orEmpty(),
            thumbnail = it.thumbs?.thumbnail.orEmpty()
        )
    }
}