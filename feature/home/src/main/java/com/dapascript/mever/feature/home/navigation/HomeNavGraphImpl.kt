package com.dapascript.mever.feature.home.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.feature.home.screen.HomeLandingScreen
import javax.inject.Inject

class HomeNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<HomeLandingRoute>(
            metadata = metadata {
                put(NavDisplay.TransitionKey) {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(800)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                }
            }
        ) { HomeLandingScreen(navigator) }
    }
}