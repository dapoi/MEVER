package com.dapascript.mever.feature.home.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverImageAttr.getBitmapFromUrl
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp20
import com.dapascript.mever.core.common.util.isMusic
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.data.model.local.ContentEntity

@Composable
internal fun HandleBottomSheetDownload(
    listContent: List<ContentEntity>,
    showBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    onClickDownload: (String) -> Unit,
    onClickDismiss: () -> Unit
) {
    var chooseQualityIndex by remember { mutableIntStateOf(0) }
    val videos = remember(listContent) {
        listContent.filter { it.type.contains("mp4") }
    }
    val images = remember(listContent) {
        listContent.filter { it.type.contains("jpg") }
    }
    val groupedContent = remember(listContent) {
        buildList {
            when {
                videos.isNotEmpty() && images.isNotEmpty() -> add(
                    listContent.first().copy(
                        fileName = "Mixed",
                        url = listContent.joinToString(",") { it.url }
                    )
                )

                images.size > 1 -> add(
                    listContent.first().copy(
                        fileName = "All Images",
                        url = images.joinToString(",") { it.url }
                    )
                )

                else -> addAll(listContent)
            }
        }
    }

    MeverBottomSheet(
        showBottomSheet = showBottomSheet,
        modifier = modifier
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                MeverImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp150)
                        .padding(bottom = Dp32, start = Dp24, end = Dp24)
                        .clip(RoundedCornerShape(Dp12)),
                    source = getImageSource(
                        url = groupedContent[chooseQualityIndex].url.split(",").first(),
                        fileName = groupedContent[chooseQualityIndex].fileName,
                        type = groupedContent[chooseQualityIndex].type,
                        urlThumbnail = groupedContent[chooseQualityIndex].thumbnail
                    )
                )
                Text(
                    text = stringResource(R.string.choose_file),
                    style = typography.bodyBold1.copy(fontSize = Sp20),
                    color = colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = Dp24)
                )
                groupedContent.forEachIndexed { index, content ->
                    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                        MeverRadioButton(
                            value = when {
                                content.quality.isNotEmpty() -> {
                                    stringResource(R.string.quality, content.quality)
                                }

                                content.fileName == "Mixed" -> {
                                    stringResource(R.string.mixed_content)
                                }

                                content.fileName == "All Images" -> {
                                    stringResource(R.string.image, images.size)
                                }

                                else -> content.fileName.ifEmpty {
                                    when {
                                        content.type.contains("mp4") -> stringResource(R.string.video)
                                        content.type.contains("mp3") -> stringResource(R.string.audio)
                                        else -> stringResource(R.string.image, 1)
                                    }
                                }
                            },
                            isChoosen = chooseQualityIndex == index,
                        ) { chooseQualityIndex = index }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp24),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dp14))
                        .onCustomClick { onClickDismiss() }
                        .weight(1f)
                        .padding(vertical = Dp16),
                    contentAlignment = Center
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = typography.bodyBold1,
                        color = colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .width(Dp2)
                        .height(Dp20)
                        .background(
                            color = colorScheme.onPrimary.copy(alpha = .08f),
                            shape = RoundedCornerShape(Dp8)
                        )
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dp14))
                        .onCustomClick {
                            val chosen = groupedContent[chooseQualityIndex]
                            if (chosen.fileName == "Mixed" || chosen.fileName == "All Images") {
                                chosen.url.split(",").forEach { url ->
                                    onClickDownload(url)
                                }
                            } else onClickDownload(chosen.url)
                        }
                        .weight(1f)
                        .padding(vertical = Dp16),
                    contentAlignment = Center
                ) {
                    Text(
                        text = stringResource(R.string.download),
                        style = typography.bodyBold1,
                        color = colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun getImageSource(
    url: String,
    fileName: String,
    type: String,
    urlThumbnail: String?
) = when {
    isMusic(fileName) && urlThumbnail.isNullOrEmpty() -> R.drawable.ic_music
    else -> {
        urlThumbnail?.takeIf { it.isNotEmpty() } ?: getBitmapFromUrl(
            url = url,
            extensionFile = type
        )
    }
}