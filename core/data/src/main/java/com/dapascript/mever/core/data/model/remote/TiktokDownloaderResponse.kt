package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class TiktokDownloaderResponse(
    @SerializedName("data") val data: DataContent
) {
    data class DataContent(
        @SerializedName("video") val video: String? = null
    )

    fun mapToEntity() = listOf(
        ContentEntity(url = data.video.orEmpty())
    )
}
