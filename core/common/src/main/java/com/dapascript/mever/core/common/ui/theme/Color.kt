package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color

val MeverPurple = Color(0xFF667AF9)
val MeverYoungPurple = Color(0xFFE0D6FB)
val MeverYellow = Color(0xFFF9D966)
val MeverBlack = Color(0xFF121212)
val MeverDarkMode = Color(0xFF292929)
val MeverDarkGray = Color(0xFF888888)
val MeverGray = Color(0xFFBAC2CD)
val MeverLightGray = Color(0xFFDCE0E6)
val MeverWhite = Color(0xFFFFFFFF)
val MeverLightMode = Color(0xFFF7F7F7)
val MeverRed = Color(0xFFD00036)
val MeverPink = Color(0xFFFBD6D6)
val MeverCreamSemiPink = Color(0xFFFBE5D6)
val MeverBlueSemiPurple = Color(0xFFD6DDFB)

sealed class MeverThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color
) {
    data object Light : MeverThemeColors(
        primary = MeverPurple,
        onPrimary = MeverBlack,
        background = MeverLightMode,
        surface = MeverWhite,
        secondary = MeverYellow,
        onSecondary = MeverWhite
    )

    data object Dark : MeverThemeColors(
        primary = MeverPurple,
        onPrimary = MeverWhite,
        background = MeverDarkMode,
        surface = MeverDarkGray,
        secondary = MeverYellow,
        onSecondary = MeverWhite
    )
}