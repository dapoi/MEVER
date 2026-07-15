package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface WaScreenRoute : NavKey {
    @Serializable
    data object WaStatusLandingRoute : WaScreenRoute
}