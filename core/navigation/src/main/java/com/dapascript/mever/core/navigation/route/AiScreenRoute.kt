package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AiScreenRoute : NavKey {
    @Serializable
    data class AiImageResultRoute(
        val prompt: String,
        val artStyle: String,
        val totalImages: Int
    ) : AiScreenRoute
}