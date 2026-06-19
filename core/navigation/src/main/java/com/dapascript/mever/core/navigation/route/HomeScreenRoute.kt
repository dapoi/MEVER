package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HomeScreenRoute : NavKey {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute
}