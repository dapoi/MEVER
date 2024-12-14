package com.dapascript.mever.feature.startup.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.StartupNavGraph
import com.dapascript.mever.core.common.navigation.graph.StartupNavGraphRoute
import com.dapascript.mever.feature.startup.navigation.route.OnboardRoute
import com.dapascript.mever.feature.startup.navigation.route.SplashRoute
import com.dapascript.mever.feature.startup.screen.OnboardScreen
import com.dapascript.mever.feature.startup.screen.SplashScreen
import javax.inject.Inject

class StartupNavGraphImpl @Inject constructor() : StartupNavGraph() {
    override fun buildGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<StartupNavGraphRoute>(startDestination = SplashRoute) {
            composable<SplashRoute> { SplashScreen(navigator) }
            composable<OnboardRoute> { OnboardScreen(navigator) }
        }
    }
}