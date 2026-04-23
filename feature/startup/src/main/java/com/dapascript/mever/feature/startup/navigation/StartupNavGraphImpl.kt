package com.dapascript.mever.feature.startup.navigation

import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import com.dapascript.mever.feature.startup.screen.OnboardScreen
import com.dapascript.mever.feature.startup.screen.SplashScreen
import javax.inject.Inject

class StartupNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composableScreen<SplashRoute> { SplashScreen(navController) }
        composableScreen<OnboardRoute>(
            enterTransition = fadeIn(spring(stiffness = StiffnessHigh)) + scaleIn(
                initialScale = .8f,
                animationSpec = spring(
                    dampingRatio = DampingRatioMediumBouncy,
                    stiffness = StiffnessMediumLow
                )
            )
        ) { OnboardScreen(navController) }
    }
}