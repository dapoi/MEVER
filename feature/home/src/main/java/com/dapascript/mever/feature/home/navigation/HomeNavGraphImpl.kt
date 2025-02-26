package com.dapascript.mever.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.extension.composableScreen
import com.dapascript.mever.core.navigation.graph.route.GraphRoute.HomeNavGraphRoute
import com.dapascript.mever.core.navigation.graph.screen.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.feature.home.screen.HomeLandingScreen
import javax.inject.Inject

class HomeNavGraphImpl @Inject constructor() : BaseNavGraph() {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<HomeNavGraphRoute>(startDestination = HomeLandingRoute) {
            composableScreen<HomeLandingRoute> { HomeLandingScreen(navController) }
        }
    }
}