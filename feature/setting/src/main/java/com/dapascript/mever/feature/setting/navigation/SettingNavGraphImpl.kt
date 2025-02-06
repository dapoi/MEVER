package com.dapascript.mever.feature.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.extension.composableScreen
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.navigation.graph.route.GraphRoute.SettingNavGraphRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingLandingRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingLanguageRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import com.dapascript.mever.feature.setting.screen.SettingLanguageScreen
import com.dapascript.mever.feature.setting.screen.SettingThemeScreen
import javax.inject.Inject

class SettingNavGraphImpl @Inject constructor() : SettingNavGraph() {
    override fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SettingNavGraphRoute>(startDestination = SettingLandingRoute) {
            composableScreen<SettingLandingRoute> { SettingLandingScreen(navigator) }
            composableScreen<SettingLanguageRoute> { SettingLanguageScreen(navigator) }
            composableScreen<SettingThemeRoute>(SettingThemeRoute::class) { SettingThemeScreen(navigator) }
        }
    }

    override fun getSettingLandingRoute(): Any {
        return SettingLandingRoute
    }
}