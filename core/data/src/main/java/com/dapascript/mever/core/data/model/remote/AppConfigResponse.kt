package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.google.gson.annotations.SerializedName

data class AppConfigResponse(
    @SerializedName("version") val version: String,
    @SerializedName("isImageGeneratorFeatureActive") val isImageGeneratorFeatureActive: Boolean,
    @SerializedName("youtubeResolutions") val youtubeResolutions: List<String>,
    @SerializedName("maintenanceDay") val maintenanceDay: String? = null
) {
    fun mapToEntity() = AppConfigEntity(
        version = version,
        isImageGeneratorFeatureActive = isImageGeneratorFeatureActive,
        youtubeResolutions = youtubeResolutions,
        maintenanceDay = maintenanceDay
    )
}