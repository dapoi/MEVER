package com.dapascript.mever.feature.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAppreciateRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.SettingAppreciateScreen
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import com.dapascript.mever.feature.setting.screen.SettingLanguageScreen
import com.dapascript.mever.feature.setting.screen.SettingThemeScreen
import javax.inject.Inject

class SettingNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composableScreen<SettingLandingRoute> { SettingLandingScreen(navController) }
        composableScreen<SettingLanguageRoute>(SettingLanguageRoute.typeMap) { SettingLanguageScreen(navController) }
        composableScreen<SettingThemeRoute> { SettingThemeScreen(navController) }
        composableScreen<SettingAppreciateRoute> { SettingAppreciateScreen(navController) }
    }
}