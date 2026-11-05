package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography

@Composable
fun MeverProBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Dp10))
            .background(colors.alwaysPurple)
            .padding(horizontal = Dp6, vertical = Dp2),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(Dp2)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
            contentDescription = null,
            tint = colors.alwaysWhite,
            modifier = Modifier.size(Dp10)
        )
        Text(
            text = stringResource(R.string.pro),
            style = typography.label3,
            color = colors.alwaysWhite,
            maxLines = 1,
            softWrap = false
        )
    }
}