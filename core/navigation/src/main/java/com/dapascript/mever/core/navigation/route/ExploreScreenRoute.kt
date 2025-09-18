package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class ExploreScreenRoute {
    @Serializable
    data object ExploreLandingRoute : ExploreScreenRoute()
}