package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.clickableSingle

@Composable
fun MeverRadioButton(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit
) {
    Box(
        modifier = modifier.clickableSingle { onCheckedChange() },
        contentAlignment = Center
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