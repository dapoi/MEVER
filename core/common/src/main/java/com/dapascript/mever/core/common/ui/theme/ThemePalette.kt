package com.dapascript.mever.core.common.ui.theme

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.R

@Keep
enum class ThemePalette(
    val titleRes: Int,
    val previewColor: Color,
    val isProOnly: Boolean
) {
    Default(
        titleRes = R.string.theme_palette_default,
        previewColor = MeverPurple,
        isProOnly = false
    ),
    Doodle(
        titleRes = R.string.theme_palette_doodle,
        previewColor = MeverBlack,
        isProOnly = true
    )
}
