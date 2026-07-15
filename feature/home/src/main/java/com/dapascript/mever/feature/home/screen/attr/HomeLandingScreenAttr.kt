package com.dapascript.mever.feature.home.screen.attr

import androidx.compose.runtime.Composable
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiBackgroundRemovalRoute
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageGeneratorRoute
import com.dapascript.mever.core.navigation.route.ExploreScreenRoute.ExploreLandingRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.AI_IMAGE
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.FIND_IMAGE
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.REMOVE_BG
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.WA
import com.dapascript.mever.core.navigation.route.WaScreenRoute.WaStatusLandingRoute

object HomeLandingScreenAttr {
    @Composable
    internal fun QuickToolsType.getCardColor() = when (this) {
        WA -> colors.lightGreenDarkGray
        REMOVE_BG -> colors.lightPurpleDarkGray
        FIND_IMAGE -> colors.lightOrangeDarkGray
        AI_IMAGE -> colors.lightPinkDarkGray
    }

    internal fun QuickToolsType.getRoute() = when (this) {
        WA -> WaStatusLandingRoute
        REMOVE_BG -> AiBackgroundRemovalRoute
        FIND_IMAGE -> ExploreLandingRoute
        AI_IMAGE -> AiImageGeneratorRoute
    }
}