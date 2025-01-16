package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import coil3.video.videoFrameMillis
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADING
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.UNKNOWN
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp22
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
import com.dapascript.mever.core.common.util.getLocalContentType
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.getTotalVideoDuration
import com.dapascript.mever.core.common.util.getTwoDecimals
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.removeExtension
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.ketch.Status.PAUSED

@Composable
fun MeverCard(
    meverCardArgs: MeverCardArgs,
    modifier: Modifier = Modifier,
) = with(meverCardArgs) {
    when (type) {
        DOWNLOADING -> MeverCardDownloading(this@with, modifier)
        DOWNLOADED -> MeverCardDownloaded(this@with, modifier)
        UNKNOWN -> {}
    }
}

@Composable
private fun MeverCardDownloading(
    meverCardArgs: MeverCardArgs,
    modifier: Modifier = Modifier
) = with(meverCardArgs) {
    val imagePainter = rememberAsyncImagePainter(if (status == PAUSED) R.drawable.ic_play else R.drawable.ic_pause)
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
            .clickableSingle { onClickDownloading?.invoke() },
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
                        platform = tag,
                        iconSize = iconSize,
                        iconPadding = iconPadding
                    )
                    Text(
                        text = fileName.replaceTimeFormat(),
                        style = typography.bodyBold2,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                }
                Text(
                    text = "Category: $tag",
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
                        style = typography.label2,
                        color = colorScheme.primary
                    )
                    Text(
                        text = "${
                            calculateDownloadedMegabytes(
                                progress = progress,
                                totalBytes = total
                            )
                        } MB/${getTwoDecimals(total / (1024.0 * 1024.0))} MB",
                        style = typography.label2,
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
                painter = imagePainter,
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
        val context = LocalContext.current
        val filePath = getMeverFiles()?.find { it.name == fileName }?.path.orEmpty()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp120)
                .clip(RoundedCornerShape(Dp16))
                .clickableSingle { onClickPlay?.invoke() }
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = Builder(context)
                    .data(filePath)
                    .isVideoContent(filePath.isVideo())
                    .crossfade(true)
                    .build(),
                contentDescription = "Thumbnail",
                contentScale = Crop
            )
            if (fileName.isVideo()) {
                Image(
                    painter = painterResource(R.drawable.ic_play_video),
                    colorFilter = tint(MeverBlack.copy(alpha = 0.6f)),
                    contentDescription = "Play",
                    modifier = Modifier
                        .size(Dp40)
                        .align(Center)
                )
                Box(
                    modifier = Modifier
                        .padding(end = Dp12, bottom = Dp12)
                        .background(color = MeverBlack.copy(alpha = 0.6f), shape = RoundedCornerShape(Dp4))
                        .width(Dp40)
                        .height(Dp22)
                        .align(BottomEnd),
                    contentAlignment = Center
                ) {
                    Text(
                        text = getTotalVideoDuration(filePath).orEmpty(),
                        style = typography.label3,
                        color = MeverWhite
                    )
                }
            } else Box(
                modifier = Modifier
                    .padding(end = Dp12, bottom = Dp12)
                    .background(color = MeverBlack.copy(alpha = 0.6f), shape = RoundedCornerShape(Dp4))
                    .size(Dp24)
                    .align(BottomEnd),
                contentAlignment = Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_image),
                    colorFilter = tint(MeverWhite),
                    contentDescription = "Image",
                    modifier = Modifier.size(Dp12)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp12),
            verticalAlignment = CenterVertically
        ) {
            MeverPlatformIcon(
                platform = tag,
                iconSize = iconSize,
                iconPadding = iconPadding
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = SpaceBetween
            ) {
                Text(
                    text = "$tag - ${fileName.replaceTimeFormat().removeExtension()}",
                    style = typography.bodyBold2,
                )
                Text(
                    text = "Type: ${getLocalContentType(filePath)}",
                    style = typography.label2,
                    color = MeverGray
                )
            }
            MeverActionButton(R.drawable.ic_share) { onClickShare?.invoke() }
            MeverActionButton(R.drawable.ic_trash) { onClickDelete?.invoke() }
        }
    }
}

private fun Builder.isVideoContent(state: Boolean): Builder = apply {
    if (state) {
        videoFrameMillis(1000)
        decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options) }
    }
}