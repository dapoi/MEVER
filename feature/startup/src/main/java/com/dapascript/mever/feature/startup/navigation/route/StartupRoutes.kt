package com.dapascript.mever.feature.startup.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class StartupRoutes {
    @Serializable
    data object SplashRoute : StartupRoutes()

    @Serializable
    data object OnboardRoute : StartupRoutes()
}