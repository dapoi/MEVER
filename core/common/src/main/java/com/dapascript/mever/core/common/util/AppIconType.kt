package com.dapascript.mever.core.common.util

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverWhite

@Keep
enum class AppIconType(
    val titleRes: Int,
    val aliasSuffix: String,
    val backgroundColor: Color,
    val icon: Int,
    val isProOnly: Boolean
) {
    Default(
        titleRes = R.string.icon_default,
        aliasSuffix = "Default",
        backgroundColor = MeverPurple,
        icon = R.drawable.ic_launcher_foreground,
        isProOnly = false
    ),
    Doodle(
        titleRes = R.string.icon_doodle,
        aliasSuffix = "Doodle",
        backgroundColor = MeverWhite,
        icon = R.drawable.ic_launcher_doodle_foreground,
        isProOnly = true
    )
}