package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppConfigResponse(
    val version: String,
    val isForceUpdateRequired: Boolean,
    val isImageGeneratorFeatureActive: Boolean,
    val isGoImgFeatureActive: Boolean,
    val showSupportedPlatform: Boolean,
    val youtubeResolutions: List<String>,
    val audioQualities: List<String>,
    val maintenanceDay: String? = null
) {
    fun mapToEntity() = AppConfigEntity(
        version = version,
        isForceUpdateRequired = isForceUpdateRequired,
        isImageGeneratorFeatureActive = isImageGeneratorFeatureActive,
        isGoImgFeatureActive = isGoImgFeatureActive,
        showSupportedPlatform = showSupportedPlatform,
        videoResolutionsAndAudioQualities = mapOf(
            "video" to youtubeResolutions,
            "audio" to audioQualities
        ),
        maintenanceDay = maintenanceDay
    )
}