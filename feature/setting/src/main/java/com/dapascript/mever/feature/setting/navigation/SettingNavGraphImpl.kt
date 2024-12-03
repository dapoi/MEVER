package com.dapascript.mever.feature.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.extension.composableScreen
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraphRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingLanding
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import javax.inject.Inject

class SettingNavGraphImpl @Inject constructor() : SettingNavGraph() {
    override fun buildGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<SettingNavGraphRoute>(startDestination = SettingLanding) {
            composableScreen<SettingLanding> { SettingLandingScreen(navigator) }
        }
    }

    override fun getSettingLandingRoute() = SettingLanding
}