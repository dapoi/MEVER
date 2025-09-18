package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppConfigResponse(
    val version: String,
    val isImageGeneratorFeatureActive: Boolean,
    val isGoImgFeatureActive: Boolean,
    val youtubeResolutions: List<String>,
    val audioQualities: List<String>,
    val maintenanceDay: String? = null
) {
    fun mapToEntity() = AppConfigEntity(
        version = version,
        isImageGeneratorFeatureActive = isImageGeneratorFeatureActive,
        isGoImgFeatureActive = isGoImgFeatureActive,
        videoResolutionsAndAudioQualities = mapOf(
            "video" to youtubeResolutions,
            "audio" to audioQualities
        ),
        maintenanceDay = maintenanceDay
    )
}