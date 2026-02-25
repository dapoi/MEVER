package com.dapascript.mever.core.navigation.route

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.navigation.helper.generateCustomNavType
import kotlinx.serialization.Serializable

sealed interface SettingScreenRoute {
    @Serializable
    data object SettingLandingRoute : SettingScreenRoute

    @Serializable
    data class SettingLanguageRoute(val languageData: LanguageData) : SettingScreenRoute {
        @Serializable
        data class LanguageData(val languageCode: String)

        companion object {
            val typeMap = mapOf(generateCustomNavType<LanguageData>())
            fun getArgs(
                savedStateHandle: SavedStateHandle
            ) = savedStateHandle.toRoute<SettingLanguageRoute>(typeMap)
        }
    }

    @Serializable
    data class SettingThemeRoute(val themeType: ThemeType) : SettingScreenRoute

    @Serializable
    data object SettingAppreciateRoute : SettingScreenRoute

    @Serializable
    data object SettingAboutAppRoute : SettingScreenRoute
}