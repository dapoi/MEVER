package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeScreenRoute : NavKey {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute

    @Serializable
    data class HomeQuickToolsRoute(val featureCards: Set<FeatureCard>) : HomeScreenRoute {
        @Serializable
        data class FeatureCard(
            val featureName: String,
            val featureDesc: String,
            val icon: Int,
            val toolsType: QuickToolsType
        ) {
            @Serializable
            enum class QuickToolsType {
                WA, REMOVE_BG, FIND_IMAGE, AI_IMAGE
            }
        }
    }
}