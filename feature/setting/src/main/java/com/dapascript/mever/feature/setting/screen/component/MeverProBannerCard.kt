package com.dapascript.mever.feature.setting.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp38
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite

@Composable
fun MeverProBannerCard(
    isProActive: Boolean,
    onClickBanner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dp16))
            .background(colors.lightPurpleDarkGray)
            .border(
                width = Dp1,
                color = MeverPurple.copy(alpha = 0.25f),
                shape = RoundedCornerShape(Dp16)
            )
            .clickable { onClickBanner() }
            .padding(horizontal = Dp16, vertical = Dp12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(Dp12)
        ) {
            Box(
                modifier = Modifier
                    .size(Dp38)
                    .clip(CircleShape)
                    .background(MeverPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
                    contentDescription = stringResource(R.string.mever_pro),
                    tint = MeverWhite,
                    modifier = Modifier.size(Dp20)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = spacedBy(Dp2)
            ) {
                Text(
                    text = stringResource(R.string.mever_pro),
                    style = typography.bodyBold1,
                    color = colors.blackWhite
                )
                Text(
                    text = stringResource(
                        if (isProActive) R.string.pro_membership_active
                        else R.string.unlock_all_premium_features
                    ),
                    style = typography.label2,
                    color = colors.blackWhite.copy(alpha = 0.65f)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp20))
                    .background(if (isProActive) MeverPurple.copy(alpha = 0.15f) else MeverPurple)
                    .clickable { onClickBanner() }
                    .padding(horizontal = Dp14, vertical = Dp6),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (isProActive) R.string.active
                        else R.string.upgrade
                    ),
                    style = typography.labelBold1,
                    color = if (isProActive) MeverPurple else MeverWhite
                )
            }
        }
    }
}