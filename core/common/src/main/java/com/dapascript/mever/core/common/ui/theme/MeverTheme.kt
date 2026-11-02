package com.dapascript.mever.core.common.ui.theme

import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.LocalColors
import com.dapascript.mever.core.common.util.LocalIsDarkMode
import com.dapascript.mever.core.common.util.LocalThemePalette
import com.dapascript.mever.core.common.util.LocalTypography

@Keep
enum class ThemeType(val themeResId: Int) {
    System(R.string.system_default),
    Light(R.string.light),
    Dark(R.string.dark)
}

object MeverThemeAttr {
    val typography: MeverTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
    val colors: MeverColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
    val isDarkMode: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkMode.current
    val themePalette: ThemePalette
        @Composable
        @ReadOnlyComposable
        get() = LocalThemePalette.current
    val isDoodle: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalThemePalette.current == ThemePalette.Doodle
}

@Composable
fun MeverTheme(
    deviceType: DeviceType,
    isDarkMode: Boolean = isSystemInDarkTheme(),
    themePalette: ThemePalette = ThemePalette.Default,
    content: @Composable () -> Unit
) {
    val fontFamily = if (themePalette == ThemePalette.Doodle) comicSansFamily else poppinsFamily
    val meverTypography = MeverTypography(deviceType = deviceType, fontFamily = fontFamily)
    val customColors = MeverColors.getColors(isDarkMode, themePalette)
    val currentDensity = LocalDensity.current

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = currentDensity.density,
            fontScale = 1f
        ),
        LocalTypography provides meverTypography,
        LocalColors provides customColors,
        LocalIsDarkMode provides isDarkMode,
        LocalThemePalette provides themePalette
    ) { MaterialTheme(content = content) }
}