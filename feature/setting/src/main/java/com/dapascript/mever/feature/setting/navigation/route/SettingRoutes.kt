package com.dapascript.mever.feature.setting.navigation.route

import com.dapascript.mever.core.common.ui.theme.ThemeType
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingRoutes {

    @Serializable
    data object SettingLandingRoute : SettingRoutes()

    @Serializable
    data class SettingLanguageRoute(val languageData: LanguageData) : SettingRoutes() {
        @Serializable
        data class LanguageData(val languageCode: String) : SettingRoutes()
    }

    @Serializable
    data class SettingThemeRoute(val themeType: ThemeType) : SettingRoutes()
}