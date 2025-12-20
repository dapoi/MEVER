package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PixivDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(val images: List<String>? = null)

    fun mapToEntity() = data?.images?.map { url ->
        ContentEntity(
            url = url,
            status = status ?: true,
            type = "jpg"
        )
    }
}