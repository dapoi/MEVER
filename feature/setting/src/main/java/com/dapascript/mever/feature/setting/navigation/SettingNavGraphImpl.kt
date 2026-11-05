package com.dapascript.mever.feature.setting.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAboutAppRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingFaqRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingPaywallRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingQuickDownloadRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingSubscriptionDetailRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.SettingAboutAppScreen
import com.dapascript.mever.feature.setting.screen.SettingFaqScreen
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import com.dapascript.mever.feature.setting.screen.SettingLanguageScreen
import com.dapascript.mever.feature.setting.screen.SettingPaywallScreen
import com.dapascript.mever.feature.setting.screen.SettingQuickDownloadScreen
import com.dapascript.mever.feature.setting.screen.SettingSubscriptionDetailScreen
import com.dapascript.mever.feature.setting.screen.SettingThemeScreen
import javax.inject.Inject

internal class SettingNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<SettingLandingRoute> { args -> SettingLandingScreen(navigator, args) }
        entry<SettingLanguageRoute> { args -> SettingLanguageScreen(navigator, args) }
        entry<SettingThemeRoute> { args -> SettingThemeScreen(navigator, args) }
        entry<SettingFaqRoute> { SettingFaqScreen(navigator) }
        entry<SettingAboutAppRoute> { SettingAboutAppScreen(navigator) }
        entry<SettingSubscriptionDetailRoute> { SettingSubscriptionDetailScreen(navigator) }
        entry<SettingQuickDownloadRoute> { SettingQuickDownloadScreen(navigator) }
        entry<SettingPaywallRoute>(
            metadata = metadata {
                put(NavDisplay.TransitionKey) {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = DampingRatioNoBouncy,
                            stiffness = StiffnessVeryLow
                        )
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                }
                put(NavDisplay.PopTransitionKey) {
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = DampingRatioNoBouncy,
                            stiffness = StiffnessLow
                        )
                    )
                }
            }
        ) { SettingPaywallScreen(navigator) }
    }
}