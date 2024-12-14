package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color

val MeverPurple = Color(0xFF667AF9)
val MeverYoungPurple = Color(0xFFE0D6FB)
val MeverYellow = Color(0xFFF9D966)
val MeverBlack = Color(0xFF121212)
val MeverDarkMode = Color(0xFF292929)
val MeverGray = Color(0xFF888888)
val MeverLightGray = Color(0xFFBAC2CD)
val MeverDarkGray = Color(0xFF535454)
val MeverWhite = Color(0xFFFFFFFF)
val MeverLightMode = Color(0xFFF7F7F7)
val MeverRed = Color(0xFFD00036)
val MeverPink = Color(0xFFFBD6D6)
val MeverCreamSemiPink = Color(0xFFFBE5D6)
val MeverBlueSemiPurple = Color(0xFFD6DDFB)
val MeverTransparent = Color(0x00000000)

sealed class MeverThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val surface: Color,
    val onSurfaceVariant: Color,
    val background: Color
) {
    data object Light : MeverThemeColors(
        primary = MeverPurple,
        onPrimary = MeverBlack,
        onPrimaryContainer = MeverWhite,
        secondary = MeverGray,
        onSecondary = MeverWhite,
        surface = MeverWhite,
        onSurfaceVariant = MeverWhite,
        background = MeverLightMode
    )

    data object Dark : MeverThemeColors(
        primary = MeverPurple,
        onPrimary = MeverWhite,
        onPrimaryContainer = MeverDarkMode,
        secondary = MeverLightGray,
        onSecondary = MeverWhite,
        surface = MeverGray,
        onSurfaceVariant = MeverLightGray,
        background = MeverDarkMode
    )
}