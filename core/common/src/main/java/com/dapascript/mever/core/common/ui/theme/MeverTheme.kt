package com.dapascript.mever.core.common.ui.theme

import android.os.Build
import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverThemeColors.Dark
import com.dapascript.mever.core.common.ui.theme.MeverThemeColors.Light

val LightColorScheme = lightColorScheme(
    primary = Light.primary,
    onPrimary = Light.onPrimary,
    onPrimaryContainer = Light.onPrimaryContainer,
    secondary = Light.secondary,
    onSecondary = Light.onSecondary,
    onSecondaryContainer = Light.onSecondaryContainer,
    surface = Light.surface,
    onSurface = Light.onSurface,
    onSurfaceVariant = Light.onSurfaceVariant,
    background = Light.background
)

val DarkColorScheme = darkColorScheme(
    primary = Dark.primary,
    onPrimary = Dark.onPrimary,
    onPrimaryContainer = Dark.onPrimaryContainer,
    secondary = Dark.secondary,
    onSecondary = Dark.onSecondary,
    onSecondaryContainer = Dark.onSecondaryContainer,
    surface = Dark.surface,
    onSurface = Dark.onSurface,
    onSurfaceVariant = Dark.onSurfaceVariant,
    background = Dark.background
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
}

@Composable
fun MeverTheme(
    meverTypography: MeverTypography = MeverTheme.typography,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalTypography provides meverTypography) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}