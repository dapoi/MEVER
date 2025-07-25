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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest.Builder
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverImageAttr.getBitmapFromUrl
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
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
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.calculateDownloadPercentage
import com.dapascript.mever.core.common.util.calculateDownloadedMegabytes
import com.dapascript.mever.core.common.util.convertFilename
import com.dapascript.mever.core.common.util.getContentType
import com.dapascript.mever.core.common.util.getFilePath
import com.dapascript.mever.core.common.util.getTwoDecimals
import com.dapascript.mever.core.common.util.onCustomClick
import com.ketch.Status
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.SUCCESS

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
        val animatedProgress by animateFloatAsState(
            targetValue = progress / 100f,
            animationSpec = ProgressAnimationSpec,
            label = "Progress Animation"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dp8))
                .onCustomClick(
                    onLongClick = { onClickDelete?.invoke() },
                    onClick = { onClickCard?.invoke() }
                )
        ) {
            MeverImage(
                modifier = Modifier
                    .size(width = Dp88, height = Dp86)
                    .clip(RoundedCornerShape(Dp8))
                    .align(CenterVertically)
                    .graphicsLayer {
                        scaleX = 1.5f
                        scaleY = 1.5f
                        translationX = 1.5f
                        translationY = 1.5f
                    },
                source = getImageSource(
                    status = status,
                    url = source,
                    fileName = fileName,
                    urlThumbnail = urlThumbnail
                ),
                isImageError = status == FAILED
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
                        text = convertFilename(
                            getFilePath(fileName).substringAfterLast("/").ifEmpty { fileName }
                        ),
                        style = typography.bodyBold2,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                }
                Text(
                    text = stringResource(
                        R.string.type,
                        getContentType(getFilePath(fileName))
                    ),
                    style = typography.label2,
                    color = MeverGray,
                    modifier = Modifier.padding(start = Dp1)
                )
                if (status != SUCCESS) Row(
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
                                color = MeverGray
                            )
                        }
                        Spacer(modifier = Modifier.height(Dp8))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dp5)
                                .padding(bottom = Dp2),
                            color = if (status == FAILED) MeverRed else colorScheme.primary,
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
                        buttonType = Outlined(
                            contentColor = colorScheme.primary,
                            borderColor = colorScheme.primary
                        )
                    ) { onClickShare?.invoke() }
                    MeverButton(
                        title = stringResource(R.string.delete_button),
                        buttonType = Filled(
                            backgroundColor = colorScheme.primary,
                            contentColor = MeverWhite
                        )
                    ) { onClickDelete?.invoke() }
                }
            }
        }
    }
}

@Composable
private fun getImageSource(
    status: Status,
    url: String,
    fileName: String,
    urlThumbnail: String?
) = when {
    status != SUCCESS -> urlThumbnail?.takeIf { it.isNotEmpty() } ?: getBitmapFromUrl(url)
    else -> getFilePath(fileName)
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