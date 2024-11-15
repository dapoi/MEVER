package com.dapascript.mever.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.HomeNavGraph
import com.dapascript.mever.core.common.navigation.graph.HomeNavGraphRoute
import com.dapascript.mever.feature.home.navigation.route.HomeLanding
import com.dapascript.mever.feature.home.screen.HomeScreen
import javax.inject.Inject

class HomeNavGraphImpl @Inject constructor() : HomeNavGraph() {
    override fun buildGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<HomeNavGraphRoute>(startDestination = HomeLanding) {
            composable<HomeLanding> { HomeScreen(navigator) }
        }
    }

    override fun getHomeRoute() = HomeLanding
}