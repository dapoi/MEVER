package com.dapascript.mever.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.navigation.base.BaseNavigator
import com.dapascript.mever.core.navigation.extension.composableScreen
import com.dapascript.mever.core.navigation.graph.HomeNavGraph
import com.dapascript.mever.core.navigation.graph.route.GraphRoute.HomeNavGraphRoute
import com.dapascript.mever.feature.home.navigation.route.HomeLandingRoute
import com.dapascript.mever.feature.home.screen.HomeLandingScreen
import javax.inject.Inject

class HomeNavGraphImpl @Inject constructor() : HomeNavGraph() {
    override fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<HomeNavGraphRoute>(startDestination = HomeLandingRoute) {
            composableScreen<HomeLandingRoute> { HomeLandingScreen(navigator) }
        }
    }

    override fun getHomeLandingRoute() = HomeLandingRoute
}