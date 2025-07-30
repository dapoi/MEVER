package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppConfigResponse(
    val version: String,
    val isImageGeneratorFeatureActive: Boolean,
    val youtubeResolutions: List<String>,
    val maintenanceDay: String? = null
) {
    fun mapToEntity() = AppConfigEntity(
        version = version,
        isImageGeneratorFeatureActive = isImageGeneratorFeatureActive,
        youtubeResolutions = youtubeResolutions,
        maintenanceDay = maintenanceDay
    )
}