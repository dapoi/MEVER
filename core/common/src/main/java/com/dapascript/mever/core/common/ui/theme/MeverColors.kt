package com.dapascript.mever.core.common.ui.theme

import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC
import com.dapascript.mever.core.common.ui.theme.ThemePalette.Doodle

// Neutrals
val MeverWhite = Color(0xFFFFFFFF)
val MeverBlack = Color(0xFF171717)
val MeverTransparent = Color(0x00000000)
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
val MeverLightOrange = Color(0xFFFFF8E7)
val MeverYellow = Color(0xFFF9D966)
val MeverLightGreen = Color(0xFFEDFFEC)
val MeverLightBlue = Color(0xFFD6EEFB)
val MeverPink = Color(0xFFFBD6D6)
val MeverLightPink = Color(0xFFF9E7FF)
val MeverCreamPink = Color(0xFFFBE5D6)

// Doodle
val MeverDoodleDark = Color(0xFF121212)
val MeverDoodleDarkSoft = Color(0xFF1A1A1A)
val MeverDoodleDarkSurface = Color(0xFF1E1E1E)
val MeverDoodleDarkBorder = Color(0xFF262626)
val MeverDoodleDarkGray = Color(0xFF374151)
val MeverDoodleMediumGray = Color(0xFF9CA3AF)
val MeverDoodleLightBorder = Color(0xFFD1D5DB)
val MeverDoodleLightSurface = Color(0xFFF9FAFB)
val MeverDoodleLightSoft = Color(0xFFF3F4F6)
val MeverDoodleLightGray = Color(0xFFE5E7EB)
val MeverDoodleGray = Color(0xFF6B7280)
val MeverDoodleCharcoal = Color(0xFF4B5563)

sealed class MeverColors(
    val alwaysPurple: Color,
    val alwaysWhite: Color,
    val alwaysWhiteBlack: Color,
    val alwaysGray: Color,
    val whiteBlack: Color,
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
    val lightPinkDarkGray: Color,
    val lightBlueDarkGray: Color
) {
    data object Light : MeverColors(
        alwaysPurple = MeverPurple,
        alwaysWhite = MeverWhite,
        alwaysWhiteBlack = MeverWhite,
        alwaysGray = MeverGray,
        whiteBlack = MeverWhite,
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
        lightPinkDarkGray = MeverLightPink,
        lightBlueDarkGray = MeverLightBlue
    )

    data object Dark : MeverColors(
        alwaysPurple = MeverPurple,
        alwaysWhite = MeverWhite,
        alwaysWhiteBlack = MeverWhite,
        alwaysGray = MeverGray,
        whiteBlack = MeverBlack,
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
        lightPinkDarkGray = MeverDarkGray,
        lightBlueDarkGray = MeverDarkGray
    )

    class PaletteColors(
        alwaysPurple: Color,
        alwaysWhite: Color,
        alwaysWhiteBlack: Color,
        alwaysGray: Color,
        whiteBlack: Color,
        whiteDark: Color,
        blackWhite: Color,
        grayLightGray: Color,
        whiteDarkGray: Color,
        lightGrayGray: Color,
        lightGrayDarkGray: Color,
        purpleTransparent: Color,
        lightSoftPurpleDark: Color,
        lightGreenDarkGray: Color,
        lightPurpleDarkGray: Color,
        lightOrangeDarkGray: Color,
        lightPinkDarkGray: Color,
        lightBlueDarkGray: Color
    ) : MeverColors(
        alwaysPurple = alwaysPurple,
        alwaysWhite = alwaysWhite,
        alwaysWhiteBlack = alwaysWhiteBlack,
        alwaysGray = alwaysGray,
        whiteBlack = whiteBlack,
        whiteDark = whiteDark,
        blackWhite = blackWhite,
        grayLightGray = grayLightGray,
        whiteDarkGray = whiteDarkGray,
        lightGrayGray = lightGrayGray,
        lightGrayDarkGray = lightGrayDarkGray,
        purpleTransparent = purpleTransparent,
        lightSoftPurpleDark = lightSoftPurpleDark,
        lightGreenDarkGray = lightGreenDarkGray,
        lightPurpleDarkGray = lightPurpleDarkGray,
        lightOrangeDarkGray = lightOrangeDarkGray,
        lightPinkDarkGray = lightPinkDarkGray,
        lightBlueDarkGray = lightBlueDarkGray
    )

    companion object {
        fun getColors(
            isDarkMode: Boolean,
            palette: ThemePalette = CLASSIC
        ): MeverColors {
            return when (palette) {
                CLASSIC -> if (isDarkMode) Dark else Light
                Doodle -> if (isDarkMode) {
                    PaletteColors(
                        alwaysPurple = MeverWhite,
                        alwaysWhite = MeverWhite,
                        alwaysWhiteBlack = MeverBlack,
                        alwaysGray = MeverDoodleMediumGray,
                        whiteBlack = MeverBlack,
                        whiteDark = MeverDoodleDark,
                        blackWhite = MeverWhite,
                        grayLightGray = MeverDoodleLightBorder,
                        whiteDarkGray = MeverDoodleDarkSurface,
                        lightGrayGray = MeverDoodleDarkGray,
                        lightGrayDarkGray = MeverDoodleDarkBorder,
                        purpleTransparent = MeverTransparent,
                        lightSoftPurpleDark = MeverDoodleDarkSoft,
                        lightGreenDarkGray = MeverDoodleDarkSurface,
                        lightPurpleDarkGray = MeverDoodleDarkSurface,
                        lightOrangeDarkGray = MeverDoodleDarkSurface,
                        lightPinkDarkGray = MeverDoodleDarkSurface,
                        lightBlueDarkGray = MeverDoodleDarkSurface
                    )
                } else {
                    PaletteColors(
                        alwaysPurple = MeverBlack,
                        alwaysWhite = MeverWhite,
                        alwaysWhiteBlack = MeverWhite,
                        alwaysGray = MeverDoodleGray,
                        whiteBlack = MeverWhite,
                        whiteDark = MeverWhite,
                        blackWhite = MeverBlack,
                        grayLightGray = MeverDoodleCharcoal,
                        whiteDarkGray = MeverDoodleLightSurface,
                        lightGrayGray = MeverDoodleLightGray,
                        lightGrayDarkGray = MeverDoodleLightGray,
                        purpleTransparent = MeverBlack,
                        lightSoftPurpleDark = MeverDoodleLightSoft,
                        lightGreenDarkGray = MeverDoodleLightSoft,
                        lightPurpleDarkGray = MeverDoodleLightSoft,
                        lightOrangeDarkGray = MeverDoodleLightSoft,
                        lightPinkDarkGray = MeverDoodleLightSoft,
                        lightBlueDarkGray = MeverDoodleLightSoft
                    )
                }
            }
        }
    }
}