package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import coil3.video.videoFrameMillis
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp86
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp88
import com.dapascript.mever.core.common.ui.theme.MeverGray
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverRed
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.calculateDownloadPercentage
import com.dapascript.mever.core.common.util.calculateDownloadedMegabytes
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.getLocalContentType
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.getTwoDecimals
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.ketch.Status
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED

@Composable
fun MeverCard(
    cardArgs: MeverCardArgs,
    modifier: Modifier = Modifier,
    onClickCard: (() -> Unit)? = null,
    onClickShare: (() -> Unit)? = null,
    onClickDelete: (() -> Unit)? = null
) = with(cardArgs) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dp24)
    ) {
        val context = LocalContext.current
        val animatedProgress by animateFloatAsState(
            targetValue = progress / 100f,
            animationSpec = ProgressAnimationSpec,
            label = "Progress Animation"
        )
        val filePath = getMeverFiles()?.find { it.name == fileName }?.path.orEmpty()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dp8))
                .clickableSingle { onClickCard?.invoke() }
        ) {
            if (progress < 100 && urlThumbnail.isNullOrEmpty()) MeverUrlThumbnail(
                source = source,
                isFailedFetchImage = status == FAILED,
                modifier = Modifier
                    .size(width = Dp88, height = Dp86)
                    .clip(RoundedCornerShape(Dp8))
                    .align(CenterVertically)
            ) else MeverLocalThumbnail(
                source = Builder(context)
                    .setThumbnail(progress, filePath, urlThumbnail.orEmpty())
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .size(width = Dp88, height = Dp86)
                    .clip(RoundedCornerShape(Dp8))
                    .align(CenterVertically)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    icon?.let { icon ->
                        MeverIcon(
                            icon = icon,
                            iconBackgroundColor = iconBackgroundColor ?: MeverGray,
                            iconSize = Dp24,
                            iconPadding = Dp5
                        )
                    }
                    Text(
                        text = "$tag - ${fileName.replaceTimeFormat()}",
                        style = typography.bodyBold2,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                }
                Text(
                    text = stringResource(R.string.type, getLocalContentType(filePath)),
                    style = typography.label2,
                    color = MeverGray
                )
                if (progress < 100) Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dp12),
                    horizontalArrangement = spacedBy(Dp8)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = Bottom
                        ) {
                            if (status == FAILED) Text(
                                text = stringResource(R.string.failed),
                                style = typography.label2,
                                color = MeverRed
                            ) else Text(
                                text = calculateDownloadPercentage(
                                    downloadedBytes = (progress / 100.0 * total).toLong(),
                                    totalBytes = total
                                ),
                                style = typography.label2,
                                color = colorScheme.primary
                            )
                            if (status != FAILED) Text(
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
                        Spacer(modifier = Modifier.height(Dp8))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dp5)
                                .padding(bottom = Dp2),
                            color = getStatusDownloadColor(status),
                            trackColor = MeverLightGray,
                            gapSize = -Dp15,
                            strokeCap = Round,
                            progress = { if (status == FAILED) 100f else animatedProgress },
                            drawStopIndicator = {}
                        )
                    }
                    Image(
                        modifier = Modifier
                            .padding(start = Dp8)
                            .size(Dp24)
                            .align(Bottom),
                        painter = getImagePainter(status),
                        colorFilter = tint(colorScheme.onPrimary),
                        contentDescription = "Play/Pause/Retry"
                    )
                } else Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dp16),
                    horizontalArrangement = spacedBy(Dp8)
                ) {
                    MeverButton(
                        title = stringResource(R.string.share),
                        buttonType = OUTLINED
                    ) { onClickShare?.invoke() }
                    MeverButton(
                        title = stringResource(R.string.delete_button),
                        buttonType = FILLED
                    ) { onClickDelete?.invoke() }
                }
            }
        }
    }
}

@Composable
private fun getImagePainter(status: Status) = rememberAsyncImagePainter(
    Builder(LocalPlatformContext.current).data(
        when (status) {
            FAILED -> R.drawable.ic_refresh
            PAUSED -> R.drawable.ic_play
            else -> R.drawable.ic_pause
        }
    ).build()
)

@Composable
private fun getStatusDownloadColor(status: Status) = if (status == FAILED) MeverRed else colorScheme.primary

private fun Builder.setThumbnail(
    progress: Int,
    localPath: String,
    urlThumbnail: String
): Builder = apply {
    data(if (progress < 100) urlThumbnail else localPath).takeIf {
        progress == 100 && localPath.isVideo()
    }?.apply {
        videoFrameMillis(1000)
        decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options) }
    }
}