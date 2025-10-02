package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FacebookDownloaderResponse(
    val status: Boolean? = null,
    val data: List<DataContent>? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        val url: String? = null,
        val quality: String? = null,
        val response: Int? = null
    )

    fun mapToEntity() = data?.filter { it.response == 200 }?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            status = status ?: true,
            quality = it.quality.orEmpty()
        )
    }
}