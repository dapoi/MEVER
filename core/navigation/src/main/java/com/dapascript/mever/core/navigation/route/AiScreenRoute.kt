package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AiScreenRoute : NavKey {
    @Serializable
    data object AiImageGeneratorRoute : AiScreenRoute

    @Serializable
    data class AiImageGeneratorResultRoute(
        val prompt: String,
        val artStyle: String
    ) : AiScreenRoute

    @Serializable
    data object AiBackgroundRemovalRoute : AiScreenRoute
}