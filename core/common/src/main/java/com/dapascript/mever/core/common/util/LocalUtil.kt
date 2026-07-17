package com.dapascript.mever.core.common.util

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf
import com.dapascript.mever.core.common.ui.theme.MeverColors
import com.dapascript.mever.core.common.ui.theme.MeverTypography
import com.dapascript.mever.core.common.util.DeviceType.PHONE

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { noLocalProvided() }
val LocalColors = staticCompositionLocalOf<MeverColors> { error("No colors provided") }
val LocalDeviceType = staticCompositionLocalOf { PHONE }
val LocalTypography = staticCompositionLocalOf { MeverTypography() }
val LocalIsDarkMode = staticCompositionLocalOf { false }

private fun noLocalProvided(): Nothing {
    error("CompositionLocal LocalActivity not present")
}
