package com.dapascript.mever.core.navigation.graph.route.screen

import com.dapascript.mever.core.common.ui.theme.ThemeType
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingScreenRoute {
    @Serializable
    data object SettingLandingRoute : SettingScreenRoute()

    @Serializable
    data class SettingLanguageRoute(val languageData: LanguageData) : SettingScreenRoute() {
        @Serializable
        data class LanguageData(val languageCode: String) : SettingScreenRoute()
    }

    @Serializable
    data class SettingThemeRoute(val themeType: ThemeType) : SettingScreenRoute()
}