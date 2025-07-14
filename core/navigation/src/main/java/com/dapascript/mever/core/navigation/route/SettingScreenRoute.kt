package com.dapascript.mever.core.navigation.route

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.navigation.helper.generateCustomNavType
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingScreenRoute {
    @Serializable
    data object SettingLandingRoute

    @Serializable
    data class SettingLanguageRoute(val languageData: LanguageData) : SettingScreenRoute() {
        @Serializable
        data class LanguageData(val languageCode: String) : SettingScreenRoute()

        companion object {
            val typeMap = mapOf(generateCustomNavType<LanguageData>())
            fun getArgs(savedStateHandle: SavedStateHandle) = savedStateHandle.toRoute<SettingLanguageRoute>(typeMap)
        }
    }

    @Serializable
    data class SettingThemeRoute(val themeType: ThemeType) : SettingScreenRoute()

    @Serializable
    data object SettingAppreciateRoute
}