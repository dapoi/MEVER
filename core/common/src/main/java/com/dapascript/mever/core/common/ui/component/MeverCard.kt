package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Max
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADING
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp88
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverGray
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.calculateDownloadPercentage
import com.dapascript.mever.core.common.util.calculateDownloadedMegabytes
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.getContentType
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.getTwoDecimals
import com.ketch.Status.PAUSED

@Composable
fun MeverCard(
    meverCardArgs: MeverCardArgs,
    modifier: Modifier = Modifier,
) = with(meverCardArgs) {
    when (type) {
        DOWNLOADING -> MeverCardDownloading(this@with, modifier)
        DOWNLOADED -> MeverCardDownloaded(this@with, modifier)
    }
}

@Composable
private fun MeverCardDownloading(
    meverCardArgs: MeverCardArgs,
    modifier: Modifier = Modifier
) = with(meverCardArgs) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = ProgressAnimationSpec,
        label = "Progress Animation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Max)
            .clip(RoundedCornerShape(Dp12))
            .clickableSingle { onDownloadingClick?.invoke() },
        shape = RoundedCornerShape(Dp12),
        colors = cardColors(colorScheme.background),
        elevation = cardElevation(Dp2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp4)
        ) {
            MeverThumbnail(
                source = image,
                modifier = Modifier
                    .width(Dp88)
                    .height(Dp80)
                    .clip(RoundedCornerShape(Dp8))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = Dp16)
                    .weight(1f),
                verticalArrangement = SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(Dp10)
                ) {
                    MeverPlatformIcon(
                        platform = fileName,
                        modifier = Modifier.size(Dp24)
                    )
                    Text(
                        text = fileName,
                        style = typography.bodyBold3,
                        maxLines = 2,
                        overflow = Ellipsis
                    )
                }
                Text(
                    text = "Type: ${getContentType(path)}",
                    style = typography.label2,
                    color = MeverGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = Bottom
                ) {
                    Text(
                        text = calculateDownloadPercentage(
                            downloadedBytes = (progress / 100.0 * total).toLong(),
                            totalBytes = total
                        ),
                        style = typography.label3,
                        color = colorScheme.primary
                    )
                    Text(
                        text = "${
                            calculateDownloadedMegabytes(
                                progress = progress,
                                totalBytes = total
                            )
                        } MB/${getTwoDecimals(total / (1024.0 * 1024.0))} MB",
                        style = typography.label3,
                        color = MeverLightGray
                    )
                }
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp5)
                        .padding(bottom = Dp2),
                    color = colorScheme.primary,
                    trackColor = MeverLightGray,
                    gapSize = -Dp15,
                    strokeCap = Round,
                    progress = { animatedProgress },
                    drawStopIndicator = {}
                )
            }
            Image(
                modifier = Modifier
                    .padding(start = Dp8)
                    .size(Dp24)
                    .align(Bottom),
                painter = painterResource(if (status == PAUSED) R.drawable.ic_play else R.drawable.ic_pause),
                colorFilter = tint(colorScheme.onPrimary),
                contentDescription = "Play/Pause"
            )
        }
    }
}

@Composable
private fun MeverCardDownloaded(
    meverCardArgs: MeverCardArgs,
    modifier: Modifier = Modifier
) = with(meverCardArgs) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = spacedBy(Dp16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp150)
                .clip(RoundedCornerShape(Dp16))
                .clickableSingle { onPlayClick?.invoke() },
            contentAlignment = Center
        ) {
            MeverThumbnail(
                modifier = Modifier.fillMaxSize(),
                source = getMeverFiles()?.find { it.name == fileName }?.path.orEmpty()
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MeverBlack.copy(alpha = 0.3f)
            ) {}
            Image(
                painter = painterResource(R.drawable.ic_play_video),
                colorFilter = tint(MeverWhite.copy(alpha = 0.7f)),
                contentDescription = "Play",
                modifier = Modifier.size(Dp80)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp12)
        ) {
            MeverPlatformIcon(
                platform = fileName,
                modifier = Modifier.size(Dp40)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = SpaceBetween
            ) {
                Text(
                    text = fileName,
                    style = typography.bodyBold3,
                )
                Text(
                    text = "Type: ${getContentType(path)}",
                    style = typography.label3,
                    color = MeverGray
                )
            }
            MeverActionButton(R.drawable.ic_share) { onShareContentClick?.invoke() }
            MeverActionButton(R.drawable.ic_trash) { onDeleteContentClick?.invoke() }
        }
    }
}