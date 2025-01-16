package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color

val MeverPurple = Color(0xFF667AF9)
val MeverLightPurple = Color(0xFFE0D6FB)
val MeverYellow = Color(0xFFF9D966)
val MeverBlack = Color(0xFF121212)
val MeverGray = Color(0xFF888888)
val MeverLightGray = Color(0xFFBAC2CD)
val MeverDark = Color(0xFF292929)
val MeverDarkGray = Color(0xFF535454)
val MeverWhite = Color(0xFFFFFFFF)
val MeverWhiteSemiPink = Color(0xFFF4F5FF)
val MeverRed = Color(0xFFD00036)
val MeverPink = Color(0xFFFBD6D6)
val MeverLightBlue = Color(0xFFD6EEFB)
val MeverCreamSemiPink = Color(0xFFFBE5D6)
val MeverLightGreen = Color(0xFFD6FBE5)
val MeverTransparent = Color(0x00000000)

sealed class MeverThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val onSecondaryContainer: Color,
    val surface: Color,
    val onSurface: Color,
    val background: Color
) {
    data object Light : MeverThemeColors(
        primary = MeverPurple,
        onPrimary = MeverBlack,
        onPrimaryContainer = MeverWhite,
        secondary = MeverGray,
        onSecondary = MeverWhite,
        onSecondaryContainer = MeverWhiteSemiPink,
        surface = MeverWhite,
        onSurface = MeverLightGray,
        background = MeverWhite
    )

    data object Dark : MeverThemeColors(
        primary = MeverPurple,
        onPrimary = MeverWhite,
        onPrimaryContainer = MeverDark,
        secondary = MeverLightGray,
        onSecondary = MeverWhite,
        onSecondaryContainer = MeverDarkGray,
        surface = MeverDarkGray,
        onSurface = MeverGray,
        background = MeverDark
    )
}