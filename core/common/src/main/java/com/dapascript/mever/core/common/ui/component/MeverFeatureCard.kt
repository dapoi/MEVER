package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.DpHalf
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverFeatureCard(
    icon: Int,
    title: String,
    desc: String,
    cardColor: Color,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    titleStyle: TextStyle = typography.bodyBold3,
    descStyle: TextStyle = typography.label3,
    onClick: () -> Unit
) {
    val isDarkMode = MeverTheme.isDarkMode
    Box(
        modifier = modifier
            .border(
                width = DpHalf,
                color = cardColor.copy(alpha = if (isDarkMode) 0.3f else 0.8f),
                shape = RoundedCornerShape(Dp12)
            )
            .background(
                color = colors.whiteDarkGray,
                shape = RoundedCornerShape(Dp12)
            )
            .clip(RoundedCornerShape(Dp12))
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            cardColor.copy(alpha = if (isDarkMode) 0.5f else 0.95f),
                            Color.Transparent
                        ),
                        center = Offset(size.width / 2f, size.height),
                        radius = size.width * 0.8f
                    ),
                    center = Offset(size.width / 2f, size.height),
                    radius = size.width * 0.8f
                )
            }
            .onCustomClick { onClick() }
            .padding(Dp4)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp12)
        ) {
            Box(
                modifier = Modifier
                    .size(Dp40)
                    .clip(RoundedCornerShape(Dp8))
                    .background(color = if (isDarkMode) MeverDark else cardColor.copy(alpha = 0.5f)),
                contentAlignment = Center
            ) {
                MeverImage(
                    modifier = Modifier.size(Dp24),
                    source = icon
                )
            }
            Spacer(modifier = Modifier.size(Dp12))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = titleStyle,
                color = colors.blackWhite
            )
            Spacer(modifier = Modifier.size(Dp4))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = desc,
                style = descStyle,
                maxLines = maxLines,
                overflow = Ellipsis,
                color = colors.alwaysGray
            )
        }
    }
}