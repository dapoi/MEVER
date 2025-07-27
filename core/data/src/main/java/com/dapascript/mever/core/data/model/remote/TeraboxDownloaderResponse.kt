package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class TeraboxDownloaderResponse(
    @SerializedName("data") val data: List<DataContent>? = null
) {
    data class DataContent(
        @SerializedName("thumbs") val thumbs: Thumbs? = null,
        @SerializedName("url" ) val url: String? = null
    ) {
        data class Thumbs(
            @SerializedName("url1") val thumbnail: String? = null
        )
    }

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            thumbnail = it.thumbs?.thumbnail.orEmpty()
        )
    }
}