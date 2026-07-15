package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color

// Neutrals
val MeverWhite = Color(0xFFFFFFFF)
val MeverBlack = Color(0xFF121212)
val MeverTransparent = Color(0x00000000)

// Darks & Grays
val MeverDark = Color(0xFF1C1C1C)
val MeverDarkGray = Color(0xFF2C2F34)
val MeverGray = Color(0xFF8F9095)
val MeverLightGray = Color(0xFFDCE0E6)

// Colorfuls
val MeverPurple = Color(0xFF667AF9)
val MeverViolet = Color(0xFFE0E4FF)
val MeverLightPurple = Color(0xFFF0F2FE)
val MeverLightSoftPurple = Color(0xFFF4F5FF)
val MeverRed = Color(0xFFFF0000)
val MeverOrange = Color(0xFFFFA500)
val MeverLightOrange = Color(0xFFFFF8E7)
val MeverYellow = Color(0xFFF9D966)
val MeverLightGreen = Color(0xFFEDFFEC)
val MeverLightBlue = Color(0xFFD6EEFB)
val MeverPink = Color(0xFFFBD6D6)
val MeverLightPink = Color(0xFFF9E7FF)
val MeverCreamPink = Color(0xFFFBE5D6)

sealed class MeverColors(
    val alwaysPurple: Color,
    val alwaysWhite: Color,
    val alwaysGray: Color,
    val whiteDark: Color,
    val blackWhite: Color,
    val grayLightGray: Color,
    val whiteDarkGray: Color,
    val lightGrayGray: Color,
    val lightGrayDarkGray: Color,
    val purpleTransparent: Color,
    val lightSoftPurpleDark: Color,
    val lightGreenDarkGray: Color,
    val lightPurpleDarkGray: Color,
    val lightOrangeDarkGray: Color,
    val lightPinkDarkGray: Color
) {
    data object Light : MeverColors(
        alwaysPurple = MeverPurple,
        alwaysWhite = MeverWhite,
        alwaysGray = MeverGray,
        whiteDark = MeverWhite,
        blackWhite = MeverBlack,
        grayLightGray = MeverGray,
        whiteDarkGray = MeverWhite,
        lightGrayGray = MeverLightGray,
        lightGrayDarkGray = MeverLightGray,
        purpleTransparent = MeverPurple,
        lightSoftPurpleDark = MeverLightSoftPurple,
        lightGreenDarkGray = MeverLightGreen,
        lightPurpleDarkGray = MeverLightPurple,
        lightOrangeDarkGray = MeverLightOrange,
        lightPinkDarkGray = MeverLightPink
    )

    data object Dark : MeverColors(
        alwaysPurple = MeverPurple,
        alwaysWhite = MeverWhite,
        alwaysGray = MeverGray,
        whiteDark = MeverDark,
        blackWhite = MeverWhite,
        grayLightGray = MeverLightGray,
        whiteDarkGray = MeverDarkGray,
        lightGrayGray = MeverGray,
        lightGrayDarkGray = MeverDarkGray,
        purpleTransparent = MeverTransparent,
        lightSoftPurpleDark = MeverDark,
        lightGreenDarkGray = MeverDarkGray,
        lightPurpleDarkGray = MeverDarkGray,
        lightOrangeDarkGray = MeverDarkGray,
        lightPinkDarkGray = MeverDarkGray
    )
}
