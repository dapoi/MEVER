package com.dapascript.mever.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.feature.home.screen.HomeImageGeneratorResultScreen
import com.dapascript.mever.feature.home.screen.HomeLandingScreen
import javax.inject.Inject

class HomeNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composableScreen<HomeLandingRoute> {
            HomeLandingScreen(navController)
        }
        composableScreen<HomeImageGeneratorResultRoute> {
            HomeImageGeneratorResultScreen(navController)
        }
    }
}