package com.dapascript.mever.core.navigation.route

import com.dapascript.mever.core.common.ui.theme.ThemeType
import kotlinx.serialization.Serializable

sealed interface SettingScreenRoute {
    @Serializable
    data object SettingLandingRoute : SettingScreenRoute

    @Serializable
    data class SettingLanguageRoute(val languageCode: String): SettingScreenRoute

    @Serializable
    data class SettingThemeRoute(val themeType: ThemeType) : SettingScreenRoute

    @Serializable
    data object SettingAppreciateRoute : SettingScreenRoute

    @Serializable
    data object SettingAboutAppRoute : SettingScreenRoute
}