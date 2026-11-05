package com.dapascript.mever.core.common.util

import androidx.compose.runtime.Composable
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr

@Composable
fun getDoodleEmptyStateDrawable(
    image: Int = R.drawable.ic_empty_state
): Int {
    val isDoodle = MeverThemeAttr.isDoodle
    val isDarkMode = MeverThemeAttr.isDarkMode
    return when {
        isDoodle && (image == R.drawable.ic_empty_state || image == R.drawable.ic_empty_state_doodle || image == R.drawable.ic_empty_state_doodle_dark) -> {
            if (isDarkMode) R.drawable.ic_empty_state_doodle_dark
            else R.drawable.ic_empty_state_doodle
        }
        else -> image
    }
}

@Composable
fun getDoodleErrorStateDrawable(
    image: Int = R.drawable.ic_error_state
): Int {
    val isDoodle = MeverThemeAttr.isDoodle
    val isDarkMode = MeverThemeAttr.isDarkMode
    return when {
        isDoodle && (image == R.drawable.ic_error_state || image == R.drawable.ic_error_state_doodle || image == R.drawable.ic_error_state_doodle_dark) -> {
            if (isDarkMode) R.drawable.ic_error_state_doodle_dark
            else R.drawable.ic_error_state_doodle
        }
        else -> image
    }
}

@Composable
fun getDoodleHeaderDrawable(
    image: Int = R.drawable.ic_header
): Int {
    val isDoodle = MeverThemeAttr.isDoodle
    val isDarkMode = MeverThemeAttr.isDarkMode
    return when {
        isDoodle && (image == R.drawable.ic_header || image == R.drawable.ic_header_doodle || image == R.drawable.ic_header_doodle_dark) -> {
            if (isDarkMode) R.drawable.ic_header_doodle_dark
            else R.drawable.ic_header_doodle
        }
        else -> image
    }
}