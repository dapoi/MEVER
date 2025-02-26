package com.dapascript.mever.core.navigation.graph.route.screen

import kotlinx.serialization.Serializable

@Serializable
sealed class StartupScreenRoute {
    @Serializable
    data object SplashRoute : StartupScreenRoute()

    @Serializable
    data object OnboardRoute : StartupScreenRoute()
}