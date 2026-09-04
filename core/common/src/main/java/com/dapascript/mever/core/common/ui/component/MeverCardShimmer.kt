package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp30
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp86
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp88

@Composable
fun MeverCardShimmer(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues? = null
) {
    Row(
        modifier = modifier.then(
            paddingValues?.let { Modifier.padding(it) } ?: Modifier
        ),
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = Dp88, height = Dp86)
                .clip(RoundedCornerShape(Dp12))
                .meverShimmer(true)
        )
        Column(
            modifier = Modifier
                .padding(start = Dp16)
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dp4),
                verticalAlignment = CenterVertically,
                horizontalArrangement = spacedBy(Dp12)
            ) {
                Box(
                    modifier = Modifier
                        .size(Dp24)
                        .clip(CircleShape)
                        .meverShimmer(true)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(Dp16)
                        .clip(RoundedCornerShape(Dp4))
                        .meverShimmer(true)
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = Dp5)
                    .width(Dp120)
                    .height(Dp12)
                    .clip(RoundedCornerShape(Dp4))
                    .meverShimmer(true)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp16),
                horizontalArrangement = spacedBy(Dp12)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .width(Dp80)
                            .height(Dp28)
                            .clip(RoundedCornerShape(Dp30))
                            .meverShimmer(true)
                    )
                }
            }
        }
    }
}