package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp50
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverMenuItem(
    menuArgs: MenuItemArgs,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = with(menuArgs) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dp12)
            .clip(
                RoundedCornerShape(
                    topStart = Dp50,
                    bottomStart = Dp50,
                    topEnd = Dp8,
                    bottomEnd = Dp8
                )
            )
            .onCustomClick { onClick() },
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Row(
            horizontalArrangement = spacedBy(Dp16),
            verticalAlignment = CenterVertically
        ) {
            MeverIcon(
                icon = leadingIcon,
                iconBackgroundColor = leadingIconBackground,
                iconSize = leadingIconSize,
                iconPadding = leadingIconPadding
            )
            Text(
                text = leadingTitle,
                style = typography.bodyBold1,
                color = colorScheme.onPrimary
            )
        }
        Row(
            horizontalArrangement = spacedBy(Dp16),
            verticalAlignment = CenterVertically
        ) {
            trailingTitle?.let { title ->
                Text(
                    text = title,
                    style = typography.body2,
                    color = colorScheme.onPrimary
                )
            }
            Box(
                modifier = Modifier
                    .background(color = MeverLightGray.copy(0.1f), shape = RoundedCornerShape(Dp8))
                    .size(Dp40),
                contentAlignment = Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    colorFilter = tint(color = colorScheme.onPrimary),
                    contentDescription = "Arrow Right",
                    modifier = Modifier
                        .size(Dp16)
                        .graphicsLayer { rotationZ = 180f }
                )
            }
        }
    }
}