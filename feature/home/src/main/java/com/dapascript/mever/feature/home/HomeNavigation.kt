package com.dapascript.mever.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data object Home
data object Setting

fun NavGraphBuilder.homeScreen() {
    composable<Home> {
        HomeRoute()
    }
}
