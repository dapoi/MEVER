package com.dapascript.mever.core.common.ui.theme

import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverColors.Dark
import com.dapascript.mever.core.common.ui.theme.MeverColors.Light
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.LocalColors

val LightColorScheme = lightColorScheme(
    primary = Light.alwaysPurple,
    onPrimary = Light.alwaysWhite,
    onPrimaryContainer = Light.alwaysLightGray,
    secondary = Light.grayLightGray,
    onSecondary = Light.whiteDarkGray,
    onSecondaryContainer = Light.lightGrayDarkGray,
    background = Light.whiteDark,
    onBackground = Light.blackWhite,
    surface = Light.darkLightGray,
    surfaceContainer = Light.lightSoftPurpleBlack,
)

val DarkColorScheme = darkColorScheme(
    primary = Dark.alwaysPurple,
    onPrimary = Dark.alwaysWhite,
    onPrimaryContainer = Dark.alwaysLightGray,
    secondary = Dark.grayLightGray,
    onSecondary = Dark.whiteDarkGray,
    onSecondaryContainer = Dark.lightGrayDarkGray,
    background = Dark.whiteDark,
    onBackground = Dark.blackWhite,
    surface = Dark.darkLightGray,
    surfaceContainer = Dark.lightSoftPurpleBlack
)

@Keep
enum class ThemeType(val themeResId: Int) {
    System(R.string.system_default),
    Light(R.string.light),
    Dark(R.string.dark)
}

object MeverTheme {
    val typography: MeverTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
    val colors: MeverColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}

@Composable
fun MeverTheme(
    deviceType: DeviceType,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val meverTypography = MeverTypography(deviceType)
    val customColors = if (darkTheme) Dark else Light
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalTypography provides meverTypography,
        LocalColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}