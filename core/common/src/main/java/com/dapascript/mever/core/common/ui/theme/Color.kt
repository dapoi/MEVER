package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color

val MeverBlack = Color(0xFF121212)
val MeverCreamPink = Color(0xFFFBE5D6)
val MeverDark = Color(0xFF292929)
val MeverDarkGray = Color(0xFF535454)
val MeverGray = Color(0xFF888888)
val MeverSoftGray = Color(0xFFECEEF1)
val MeverLightBlue = Color(0xFFD6EEFB)
val MeverLightGray = Color(0xFFDCE0E6)
val MeverLightGreen = Color(0xFFD6FBE5)
val MeverLightPurple = Color(0xFFE0D6FB)
val MeverLightSoftPurple = Color(0xFFF4F5FF)
val MeverLightPink = Color(0xFFFAE9E9)
val MeverPink = Color(0xFFFBD6D6)
val MeverGreen = Color(0xFFEAFFC9)
val MeverPurple = Color(0xFF667AF9)
val MeverRed = Color(0xFFFF0000)
val MeverTransparent = Color(0x00000000)
val MeverViolet = Color(0xFFE0E4FF)
val MeverWhite = Color(0xFFFFFFFF)
val MeverSoftWhite = Color(0xFFF8F8F8)
val MeverYellow = Color(0xFFF9D966)
val MeverOrange = Color(0xFFFFA500)
val MeverLightViolet = Color(0xFFE0E4FF)

sealed class MeverColors(
    val alwaysPurple: Color,
    val alwaysWhite: Color,
    val alwaysLightGray: Color,
    val whiteDark: Color,
    val blackWhite: Color,
    val darkLightGray: Color,
    val grayLightGray: Color,
    val whiteDarkGray: Color,
    val lightGrayDarkGray: Color,
    val lightSoftPurpleBlack: Color
) {
    data object Light : MeverColors(
        alwaysPurple = MeverPurple,
        alwaysWhite = MeverWhite,
        alwaysLightGray = MeverLightGray,
        whiteDark = MeverWhite,
        blackWhite = MeverBlack,
        darkLightGray = MeverDark,
        grayLightGray = MeverGray,
        whiteDarkGray = MeverWhite,
        lightGrayDarkGray = MeverLightGray,
        lightSoftPurpleBlack = MeverLightSoftPurple
    )

    data object Dark : MeverColors(
        alwaysPurple = MeverPurple,
        alwaysWhite = MeverWhite,
        alwaysLightGray = MeverLightGray,
        whiteDark = MeverDark,
        blackWhite = MeverWhite,
        darkLightGray = MeverLightGray,
        grayLightGray = MeverLightGray,
        whiteDarkGray = MeverDarkGray,
        lightGrayDarkGray = MeverDarkGray,
        lightSoftPurpleBlack = MeverBlack
    )
}