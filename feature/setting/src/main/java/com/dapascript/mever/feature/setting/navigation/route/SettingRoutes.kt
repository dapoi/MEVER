package com.dapascript.mever.feature.setting.navigation.route

import android.os.Parcelable
import com.dapascript.mever.core.common.ui.theme.ThemeType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingRoutes {
    @Serializable
    data object SettingLandingRoute : SettingRoutes()

    @Serializable
    data class SettingLanguageRoute(val languageCode: String) : SettingRoutes()

    @Serializable
    @Parcelize
    data class SettingThemeRoute(val themeType: ThemeType) : SettingRoutes(), Parcelable
}