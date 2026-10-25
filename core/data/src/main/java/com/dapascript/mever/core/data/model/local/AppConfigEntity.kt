package com.dapascript.mever.core.data.model.local

data class AppConfigEntity(
    val enableForceUpdate: Boolean,
    val enableImageGen: Boolean,
    val enableImageFinder: Boolean,
    val enableMaintenance: Boolean,
    val showPlatforms: Boolean,
    val videoResolutionsAndAudioQualities: Map<String, List<String>>
)