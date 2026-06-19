package com.dapascript.mever.feature.gallery.navigation

import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.gallery.screen.GalleryContentDetailScreen
import com.dapascript.mever.feature.gallery.screen.GalleryLandingScreen
import javax.inject.Inject

class GalleryNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<GalleryLandingRoute> { GalleryLandingScreen(navigator) }
        entry<GalleryContentDetailRoute>(
            metadata = metadata {
                put(NavDisplay.TransitionKey) {
                    (fadeIn(spring(stiffness = StiffnessHigh)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = spring(
                            dampingRatio = DampingRatioNoBouncy,
                            stiffness = StiffnessMediumLow
                        )
                    )) togetherWith fadeOut(spring(stiffness = StiffnessMediumLow))
                }
                put(NavDisplay.PopTransitionKey) {
                    fadeIn(spring(stiffness = StiffnessMediumLow)) togetherWith
                            (scaleOut(animationSpec = spring(stiffness = StiffnessMediumLow)) +
                                    fadeOut(spring(stiffness = StiffnessMediumLow)))
                }
            }
        ) { args ->
            GalleryContentDetailScreen(
                navigator = navigator,
                args = args
            )
        }
    }
}