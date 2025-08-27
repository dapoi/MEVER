package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.common.util.sanitizeFilename
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeraboxDownloaderResponse(
    val status: Boolean? = null,
    val data: List<DataContent>? = null
) {
    data class DataContent(
        val thumbs: Thumbs? = null,
        val url: String? = null,
        @param:Json(name = "server_filename") val serverFileName: String? = null
    ) {
        data class Thumbs(
            @param:Json(name = "url1") val thumbnail: String? = null
        )
    }

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            status = status ?: true,
            fileName = sanitizeFilename(it.serverFileName.orEmpty()),
            thumbnail = it.thumbs?.thumbnail.orEmpty()
        )
    }
}