package com.dapascript.mever.feature.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.extension.composableScreen
import com.dapascript.mever.core.common.navigation.extension.generateNavType
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraphRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingLandingRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingLanguageRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import com.dapascript.mever.feature.setting.screen.SettingLanguageScreen
import com.dapascript.mever.feature.setting.screen.SettingThemeScreen
import javax.inject.Inject
import kotlin.reflect.typeOf

class SettingNavGraphImpl @Inject constructor() : SettingNavGraph() {
    override fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SettingNavGraphRoute>(startDestination = SettingLandingRoute) {
            composableScreen<SettingLandingRoute> { SettingLandingScreen(navigator) }
            composableScreen<SettingLanguageRoute> { SettingLanguageScreen(navigator) }
            composableScreen<SettingThemeRoute>(
                typeMap = mapOf(typeOf<SettingThemeRoute>() to generateNavType<SettingThemeRoute>())
            ) { SettingThemeScreen(navigator) }
        }
    }

    override fun getSettingLandingRoute() = SettingLandingRoute
}