package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class FacebookDownloaderResponse(
    @SerializedName("data") val data: List<DataContent>? = null
) {
    data class DataContent(
        @SerializedName("url") val url: String? = null,
        @SerializedName("quality") val quality: String? = null,
        @SerializedName("response") val response: Int? = null
    )

    fun mapToEntity() = data?.filter { it.response == 200 }?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            quality = it.quality.orEmpty()
        )
    }
}