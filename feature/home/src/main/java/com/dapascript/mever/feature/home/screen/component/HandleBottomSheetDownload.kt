package com.dapascript.mever.feature.home.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp250
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
    modifier: Modifier = Modifier,
    onClickDownload: (String) -> Unit,
    onClickPreview: (Int) -> Unit,
    onClickDismiss: () -> Unit
) {
    var selectMultipleItems by remember(listContent) {
        mutableStateOf(listContent.indices.toSet())
    }
    val isMusic = remember(listContent) {
        isMusic(listContent.firstOrNull()?.fileName.orEmpty())
    }

    MeverBottomSheet(
        showBottomSheet = listContent.isNotEmpty(),
        modifier = modifier
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (isMusic) MeverImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp250)
                        .padding(bottom = Dp32, start = Dp24, end = Dp24)
                        .clip(RoundedCornerShape(Dp12)),
                    source = listContent.first().thumbnail.ifEmpty { R.drawable.ic_music }
                )
                Text(
                    text = stringResource(R.string.choose_file),
                    style = typography.bodyBold1.copy(fontSize = Sp20),
                    color = colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = Dp24)
                )
                listContent.forEachIndexed { index, content ->
                    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                        MeverCheckBoxButton(
                            value = getValueSelector(index, content),
                            isChecked = selectMultipleItems.contains(index),
                            showPreviewButton = isMusic.not(),
                            onClickPreview = { onClickPreview(index) },
                            onChooseValue = {
                                selectMultipleItems = if (selectMultipleItems.contains(index)) {
                                    selectMultipleItems - index
                                } else {
                                    selectMultipleItems + index
                                }
                            }
                        )
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
                if (selectMultipleItems.isEmpty()) return@Row
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dp14))
                        .onCustomClick {
                            selectMultipleItems.forEach { onClickDownload(listContent[it].url) }
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
private fun MeverCheckBoxButton(
    value: String,
    isChecked: Boolean,
    showPreviewButton: Boolean,
    modifier: Modifier = Modifier,
    onClickPreview: () -> Unit,
    onChooseValue: () -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(Dp8))
        .onCustomClick(onClick = { onChooseValue() })
        .padding(vertical = Dp16, horizontal = Dp24),
    verticalAlignment = CenterVertically,
    horizontalArrangement = spacedBy(Dp16)
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .onCustomClick { onChooseValue() },
        contentAlignment = Center
    ) {
        Icon(
            painter = painterResource(
                if (isChecked) R.drawable.ic_round_checked
                else R.drawable.ic_round_unchecked
            ),
            contentDescription = "Radio button",
            tint = colorScheme.primary
        )
    }
    Text(
        modifier = Modifier.weight(1f),
        text = value,
        maxLines = 2,
        overflow = Ellipsis,
        style = typography.body1,
        color = colorScheme.onPrimary
    )
    if (showPreviewButton) Text(
        modifier = Modifier
            .clip(RoundedCornerShape(Dp8))
            .onCustomClick { onClickPreview() },
        text = stringResource(R.string.preview),
        textAlign = End,
        style = typography.bodyBold2,
        color = colorScheme.primary
    )
}

@Composable
private fun getValueSelector(
    index: Int,
    content: ContentEntity
) = if (content.quality.isNotEmpty()) {
    stringResource(R.string.quality, content.quality)
} else content.fileName.ifEmpty {
    when {
        content.type.contains("mp4") -> stringResource(R.string.video)
        content.type.contains("mp3") -> stringResource(R.string.audio)
        else -> stringResource(R.string.image, index + 1)
    }
}