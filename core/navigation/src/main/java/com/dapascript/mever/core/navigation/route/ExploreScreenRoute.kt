package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface ExploreScreenRoute {
    @Serializable
    data object ExploreLandingRoute : ExploreScreenRoute
}