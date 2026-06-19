package com.dapascript.mever.feature.setting.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAboutAppRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAppreciateRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.SettingAboutAppScreen
import com.dapascript.mever.feature.setting.screen.SettingAppreciateScreen
import com.dapascript.mever.feature.setting.screen.SettingLandingScreen
import com.dapascript.mever.feature.setting.screen.SettingLanguageScreen
import com.dapascript.mever.feature.setting.screen.SettingThemeScreen
import javax.inject.Inject

class SettingNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<SettingLandingRoute> { SettingLandingScreen(navigator) }
        entry<SettingLanguageRoute> { args -> SettingLanguageScreen(navigator, args) }
        entry<SettingThemeRoute> { args -> SettingThemeScreen(navigator, args) }
        entry<SettingAppreciateRoute> { SettingAppreciateScreen(navigator) }
        entry<SettingAboutAppRoute> { SettingAboutAppScreen(navigator) }
    }
}