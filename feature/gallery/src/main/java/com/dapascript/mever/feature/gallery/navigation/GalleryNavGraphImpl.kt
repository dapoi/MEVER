package com.dapascript.mever.feature.gallery.navigation

import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.gallery.screen.GalleryContentDetailScreen
import com.dapascript.mever.feature.gallery.screen.GalleryLandingScreen
import javax.inject.Inject

class GalleryNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composableScreen<GalleryLandingRoute> { GalleryLandingScreen(navController) }
        composableScreen<GalleryContentDetailRoute>(
            enterTransition = fadeIn(spring(stiffness = StiffnessHigh)) + scaleIn(
                initialScale = .8f,
                animationSpec = spring(
                    dampingRatio = DampingRatioNoBouncy,
                    stiffness = StiffnessMediumLow
                )
            ),
            popExitTransition = scaleOut(
                animationSpec = spring(stiffness = StiffnessMediumLow)
            ) + fadeOut(spring(stiffness = StiffnessMediumLow)),
        ) { GalleryContentDetailScreen(navController) }
    }
}