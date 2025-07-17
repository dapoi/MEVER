package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class InstagramDownloaderResponse(
    @SerializedName("data") val data: List<DataContent>? = null
) {
    data class DataContent(
        @SerializedName("type") val type: String? = null,
        @SerializedName("url") val url: String? = null
    )

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            type = it.type.orEmpty()
        )
    }
}
