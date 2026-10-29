package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class AppConfigResponse(
    val enableForceUpdate: Boolean,
    val enableImageGen: Boolean,
    val enableImageFinder: Boolean,
    val enableMaintenance: Boolean,
    val showPlatforms: Boolean,
    val youtubeResolutions: List<String>,
    val audioQualities: List<String>
) {
    fun mapToEntity() = AppConfigEntity(
        enableForceUpdate = enableForceUpdate,
        enableImageGen = enableImageGen,
        enableImageFinder = enableImageFinder,
        showPlatforms = showPlatforms,
        videoResolutionsAndAudioQualities = mapOf(
            "video" to youtubeResolutions,
            "audio" to audioQualities
        ),
        enableMaintenance = enableMaintenance
    )
}