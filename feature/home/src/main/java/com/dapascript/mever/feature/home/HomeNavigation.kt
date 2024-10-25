package com.dapascript.mever.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class Home(val userId: String)
data object Setting

fun NavController.navigateToHome(userId: String, navOptions: NavOptions? = null) {
    navigate(Home(userId), navOptions)  // Type safe navigation
}

fun NavGraphBuilder.homeScreen() {
    composable<Home> {
        // Using savedStateHandle in VM can be better
        val userId = it.toRoute<Home>().userId  // Type safe access
        HomeRoute(userId)
    }
}
