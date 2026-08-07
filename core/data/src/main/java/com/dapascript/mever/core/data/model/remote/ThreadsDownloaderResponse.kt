package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ThreadsDownloaderResponse(
    val status: Boolean? = null,
    val data: List<DataContent>? = null
) {
    @JsonClass(generateAdapter = true)
    internal data class DataContent(
        val type: String? = null,
        val url: String? = null
    )

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            status = status ?: true,
            type = it.type.orEmpty()
        )
    }
}