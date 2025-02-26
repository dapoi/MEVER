package com.dapascript.mever.feature.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.helper.generateCustomNavType
import com.dapascript.mever.core.navigation.graph.route.GraphRoute.SettingNavGraphRoute
import com.dapascript.mever.core.navigation.graph.screen.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.core.navigation.graph.screen.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.graph.screen.SettingScreenRoute.SettingLanguageRoute.LanguageData
import com.dapascript.mever.core.navigation.graph.screen.SettingScreenRoute.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import com.dapascript.mever.feature.setting.screen.SettingLanguageScreen
import com.dapascript.mever.feature.setting.screen.SettingThemeScreen
import javax.inject.Inject

class SettingNavGraphImpl @Inject constructor() : BaseNavGraph() {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SettingNavGraphRoute>(startDestination = SettingLandingRoute) {
            composableScreen<SettingLandingRoute> { SettingLandingScreen(navController) }
            composableScreen<SettingLanguageRoute>(mapOf(generateCustomNavType<LanguageData>())) {
                SettingLanguageScreen(navController)
            }
            composableScreen<SettingThemeRoute> { SettingThemeScreen(navController) }
        }
    }
}