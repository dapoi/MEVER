package com.dapascript.mever.core.common.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R

@Composable
fun MeverRadioButton(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit
) {
    IconToggleButton(
        modifier = modifier,
        checked = isChecked,
        onCheckedChange = { onCheckedChange() }
    ) {
        Icon(
            painter = painterResource(
                if (isChecked) R.drawable.ic_circle_checked
                else R.drawable.ic_circle_unchecked
            ),
            contentDescription = "Radio button",
            tint = colorScheme.primary
        )
    }
}