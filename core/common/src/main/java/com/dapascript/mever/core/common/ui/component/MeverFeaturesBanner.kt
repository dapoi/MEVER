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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverFeaturesBanner(
    icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    isSingleItem: Boolean = false,
    arrowColor: Color? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
            .background(
                color = colors.whiteDarkGray,
                shape = RoundedCornerShape(Dp12)
            )
            .clip(RoundedCornerShape(Dp12))
            .onCustomClick { onClick() }
            .padding(Dp4)
    ) {
        if (isSingleItem) Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp8),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(Dp12)
        ) {
            Image(
                modifier = Modifier.size(Dp28),
                imageVector = ImageVector.vectorResource(icon),
                contentScale = Fit,
                contentDescription = title
            )
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = typography.bodyBold2,
                color = colors.darkLightGray
            )
            arrowColor?.let {
                Icon(
                    modifier = Modifier.size(Dp20),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_started),
                    tint = it,
                    contentDescription = null
                )
            }
        } else Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp8),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(Dp12)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(Dp28),
                    imageVector = ImageVector.vectorResource(icon),
                    contentScale = Fit,
                    contentDescription = title
                )
                arrowColor?.let {
                    Icon(
                        modifier = Modifier.size(Dp20),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_started),
                        tint = it,
                        contentDescription = null
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = typography.bodyBold2,
                color = colors.darkLightGray
            )
        }
    }
}