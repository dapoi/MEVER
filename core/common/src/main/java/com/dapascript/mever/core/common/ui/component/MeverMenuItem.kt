package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Default
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Switch
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp50
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverMenuItem(
    menuArgs: MenuItemArgs,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = with(menuArgs) {
    val themeColors = colors
    val themeTypography = typography
    val shape = remember {
        RoundedCornerShape(
            topStart = Dp50,
            bottomStart = Dp50,
            topEnd = Dp8,
            bottomEnd = Dp8
        )
    }
    val descriptionColor = remember(themeColors.blackWhite) { themeColors.blackWhite.copy(alpha = 0.5f) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dp12)
            .clip(shape)
            .onCustomClick { onClick() },
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = spacedBy(Dp16),
            verticalAlignment = CenterVertically
        ) {
            MeverIcon(
                icon = leadingIcon,
                iconBackgroundColor = leadingIconBackground,
                iconSize = leadingIconSize,
                iconPadding = leadingIconPadding
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = leadingTitle,
                    style = themeTypography.bodyBold1,
                    color = themeColors.blackWhite
                )
                leadingDesc?.let {
                    Text(
                        modifier = Modifier.padding(top = Dp4),
                        text = leadingDesc,
                        style = themeTypography.bodyBold3,
                        fontStyle = Italic,
                        color = descriptionColor
                    )
                }
            }
        }
        when (trailingType) {
            is Default -> {
                val trailingTitleColor = trailingType.trailingTitleColor ?: themeColors.blackWhite
                val boxBackground = remember(MeverLightGray) { MeverLightGray.copy(alpha = 0.1f) }
                Row(
                    horizontalArrangement = spacedBy(Dp16),
                    verticalAlignment = CenterVertically
                ) {
                    trailingType.trailingTitle?.let { title ->
                        Text(
                            text = title,
                            style = themeTypography.body2,
                            color = trailingTitleColor
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = boxBackground,
                                shape = RoundedCornerShape(Dp8)
                            )
                            .size(Dp40),
                        contentAlignment = Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_back),
                            colorFilter = tint(color = themeColors.blackWhite),
                            contentDescription = "Arrow Right",
                            modifier = Modifier
                                .size(Dp16)
                                .graphicsLayer { rotationZ = 180f }
                        )
                    }
                }
            }

            is Switch -> {
                MeverSwitch(
                    modifier = Modifier.align(CenterVertically),
                    isChecked = trailingType.switchState
                )
            }
        }
    }
}