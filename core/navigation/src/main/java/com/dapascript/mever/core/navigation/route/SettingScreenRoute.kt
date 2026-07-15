package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.common.ui.theme.ThemeType
import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingScreenRoute : NavKey {
    @Serializable
    data class SettingLandingRoute(val showQrisDialog: Boolean) : SettingScreenRoute

    @Serializable
    data class SettingLanguageRoute(val languageCode: String): SettingScreenRoute

    @Serializable
    data class SettingThemeRoute(val themeType: ThemeType) : SettingScreenRoute

    @Serializable
    data object SettingFaqRoute : SettingScreenRoute

    @Serializable
    data object SettingAboutAppRoute : SettingScreenRoute
}