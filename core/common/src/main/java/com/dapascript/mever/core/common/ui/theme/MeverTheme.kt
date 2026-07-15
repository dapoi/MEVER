package com.dapascript.mever.core.common.ui.theme

import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverColors.Dark
import com.dapascript.mever.core.common.ui.theme.MeverColors.Light
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.LocalColors
import com.dapascript.mever.core.common.util.LocalIsDarkMode
import com.dapascript.mever.core.common.util.LocalTypography

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
    val isDarkMode: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkMode.current
}

@Composable
fun MeverTheme(
    deviceType: DeviceType,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val meverTypography = MeverTypography(deviceType)
    val customColors = if (darkTheme) Dark else Light

    CompositionLocalProvider(
        LocalTypography provides meverTypography,
        LocalColors provides customColors,
        LocalIsDarkMode provides darkTheme
    ) { MaterialTheme(content = content) }
}