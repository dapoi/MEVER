package com.dapascript.mever.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.getCurrentRoute
import com.dapascript.mever.core.navigation.helper.navigateClearBackStack
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import com.dapascript.mever.viewmodel.MainViewModel

@Composable
internal fun MainNavigation(
    navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>,
    deviceType: DeviceType,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.navigationToHomeEvent.collect {
            if (navController.getCurrentRoute() != SplashRoute.toString()) {
                navController.navigateClearBackStack(SplashRoute)
            }
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashRoute::class
    ) {
        navGraphs.forEach { navGraph ->
            navGraph.createGraph(
                navController = navController,
                deviceType = deviceType,
                navGraphBuilder = this
            )
        }
    }
}