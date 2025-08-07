package com.dapascript.mever.core.data.model.local

data class AppConfigEntity(
    val version: String,
    val isImageGeneratorFeatureActive: Boolean,
    val videoResolutionsAndAudioQualities: Map<String, List<String>>,
    val maintenanceDay: String?
)