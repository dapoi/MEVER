package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class TikTokDownloaderResponse(
    @SerializedName("data") val data: DataVideo
) {
    data class DataVideo(
        @SerializedName("data") val subData: SubDataVideo
    ) {
        data class SubDataVideo(
            @SerializedName("play") val player: String? = null,
            @SerializedName("cover") val cover: String? = null
        )
    }

    fun mapToEntity() = listOf(
        ContentEntity(
            url = data.subData.player.orEmpty(),
            quality = "",
            thumbnail = data.subData.cover.orEmpty()
        )
    )
}
