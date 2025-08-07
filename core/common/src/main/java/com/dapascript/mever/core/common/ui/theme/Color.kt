package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color

val MeverBlack = Color(0xFF121212)
val MeverCreamPink = Color(0xFFFBE5D6)
val MeverDark = Color(0xFF292929)
val MeverDarkGray = Color(0xFF535454)
val MeverGray = Color(0xFF888888)
val MeverLightBlue = Color(0xFFD6EEFB)
val MeverLightGray = Color(0xFFDCE0E6)
val MeverLightGreen = Color(0xFFD6FBE5)
val MeverLightPurple = Color(0xFFE0D6FB)
val MeverPink = Color(0xFFFBD6D6)
val MeverLightPink = Color(0xFFFAE9E9)
val MeverPurple = Color(0xFF667AF9)
val MeverRed = Color(0xFFFF0000)
val MeverTransparent = Color(0x00000000)
val MeverViolet = Color(0xFFE0E4FF)
val MeverWhite = Color(0xFFFFFFFF)
val MeverWhiteSemiPink = Color(0xFFF4F5FF)
val MeverYellow = Color(0xFFF9D966)

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
        onPrimaryContainer = MeverBlack,
        secondary = MeverLightGray,
        onSecondary = MeverWhite,
        onSecondaryContainer = MeverBlack,
        surface = MeverDarkGray,
        onSurface = MeverGray,
        background = MeverDark
    )
}