package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ThreadsDownloaderResponse(
    val data: List<DataContent>? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val type: String? = null,
        val url: String? = null
    )

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            type = it.type.orEmpty()
        )
    }
}