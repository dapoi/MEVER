package com.dapascript.mever.core.data.model.local

data class AppConfigEntity(
    val isImageGeneratorFeatureActive: Boolean,
    val isGoImgFeatureActive: Boolean,
    val showSupportedPlatform: Boolean,
    val videoResolutionsAndAudioQualities: Map<String, List<String>>,
    val maintenanceDay: String?,
    val isForceUpdateRequired: Boolean = false
)