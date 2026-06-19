package com.dapascript.mever.feature.startup.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import com.dapascript.mever.feature.startup.screen.OnboardScreen
import com.dapascript.mever.feature.startup.screen.SplashScreen
import javax.inject.Inject

class StartupNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<SplashRoute> { SplashScreen(navigator) }
        entry<OnboardRoute>(
            metadata = metadata {
                put(NavDisplay.TransitionKey) {
                    fadeIn(spring(stiffness = StiffnessHigh)) + scaleIn(
                        initialScale = .8f,
                        animationSpec = spring(
                            dampingRatio = DampingRatioMediumBouncy,
                            stiffness = StiffnessMediumLow
                        )
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                }
            }
        ) { OnboardScreen(navigator) }
    }
}