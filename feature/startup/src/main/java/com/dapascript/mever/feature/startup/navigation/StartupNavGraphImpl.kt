package com.dapascript.mever.feature.startup.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.extension.composableScreen
import com.dapascript.mever.core.common.navigation.graph.StartupNavGraph
import com.dapascript.mever.core.common.navigation.graph.route.GraphRoute.StartupNavGraphRoute
import com.dapascript.mever.feature.startup.navigation.route.StartupRoutes.OnboardRoute
import com.dapascript.mever.feature.startup.navigation.route.StartupRoutes.SplashRoute
import com.dapascript.mever.feature.startup.screen.OnboardScreen
import com.dapascript.mever.feature.startup.screen.SplashScreen
import javax.inject.Inject

class StartupNavGraphImpl @Inject constructor() : StartupNavGraph() {
    override fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<StartupNavGraphRoute>(startDestination = SplashRoute) {
            composableScreen<SplashRoute> { SplashScreen(navigator) }
            composableScreen<OnboardRoute> { OnboardScreen(navigator) }
        }
    }
}