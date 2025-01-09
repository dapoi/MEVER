package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class TikTokDownloaderResponse(
    @SerializedName("data") val data: DataVideo
) {
    data class DataVideo(
        @SerializedName("data") val subData: SubDataVideo
    ) {
        data class SubDataVideo(
            @SerializedName("play") val player: String
        )
    }

    fun mapToEntity() = listOf(
        ContentEntity(
            url = data.subData.player,
            quality = ""
        )
    )
}
