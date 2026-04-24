package com.dapascript.mever.core.common.ui.component

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverWhite

@Composable
fun MeverSwitch(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    Switch(
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MeverWhite,
            checkedTrackColor = colors.alwaysPurple,
            uncheckedThumbColor = colors.grayLightGray,
            uncheckedTrackColor = colors.lightGrayDarkGray,
            uncheckedBorderColor = Unspecified
        ),
        checked = isChecked,
        onCheckedChange = onCheckedChange?.let { { onCheckedChange(it) } }
    )
}