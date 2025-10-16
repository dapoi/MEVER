package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIcon
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIconBackgroundColor
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography

@Composable
fun MeverPlatformSupportedItem(
    platformName: String,
    iconSize: Dp,
    iconPadding: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = spacedBy(Dp16),
        verticalAlignment = CenterVertically
    ) {
        MeverIcon(
            icon = getPlatformIcon(platformName),
            iconBackgroundColor = getPlatformIconBackgroundColor(platformName),
            iconSize = iconSize,
            iconPadding = iconPadding
        )
        Text(
            text = platformName,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}