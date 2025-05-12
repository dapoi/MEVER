package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class SavefromDownloaderResponse(
    @SerializedName("data") val data: DataVideo
) {
    data class DataVideo(
        @SerializedName("url") val url: List<UrlVideos>,
        @SerializedName("thumb") val thumb: String? = null
    ) {
        data class UrlVideos(
            @SerializedName("url") val url: String? = null,
            @SerializedName("type") val type: String? = null,
            @SerializedName("subname") val subname: String? = null
        )
    }

    fun mapToEntity() = data.url.filter { it.type.orEmpty().contains("mp3").not() }.map {
        ContentEntity(
            url = it.url.orEmpty(),
            quality = it.subname.orEmpty(),
            thumbnail = data.thumb.orEmpty(),
            type = it.type.orEmpty()
        )
    }
}
