package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp88
import com.dapascript.mever.core.common.ui.theme.MeverGray
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.calculateDownloadedMegabytes

@Composable
fun MeverCard(
    meverCardArgs: MeverCardArgs,
    modifier: Modifier = Modifier
) = with(meverCardArgs) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            MeverThumbnail(
                url = image,
                modifier = Modifier
                    .width(Dp88)
                    .height(Dp80)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = SpaceBetween
            ) {
                Row(
                    horizontalArrangement = spacedBy(Dp12),
                    verticalAlignment = CenterVertically
                ) {
                    MeverPlatformIcon(
                        type = type,
                        modifier = Modifier.size(Dp24)
                    )
                    Text(
                        text = title,
                        style = typography.h5,
                        maxLines = 2,
                        overflow = Ellipsis
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = calculateDownloadedMegabytes(progress, total),
                    style = typography.body2,
                    color = MeverGray,
                    textAlign = End
                )
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { progress / 100f }
                )
            }
        }
    }
}