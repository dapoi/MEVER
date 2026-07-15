package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ExploreScreenRoute : NavKey {
    @Serializable
    data object ExploreLandingRoute : ExploreScreenRoute
}