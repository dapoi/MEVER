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
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.component.MeverUrlThumbnail
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
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.data.model.local.ContentEntity

@Composable
internal fun HandleBottomSheetDownload(
    listContent: List<ContentEntity>,
    showBottomSheet: Boolean,
    isFailedFetchImage: Boolean,
    modifier: Modifier = Modifier,
    onClickDownload: (String) -> Unit,
    onClickDismiss: () -> Unit
) = with(listContent) {
    MeverBottomSheet(
        showBottomSheet = showBottomSheet,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = Dp24)
        ) {
            var chooseQualityIndex by remember { mutableIntStateOf(0) }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (first().thumbnail.isEmpty()) MeverUrlThumbnail(
                    source = size.takeIf { it > 0 }?.let { first().url } ?: "",
                    isFailedFetchImage = isFailedFetchImage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp150)
                        .padding(bottom = Dp32)
                        .clip(RoundedCornerShape(Dp12))
                ) else MeverImage(
                    source = first().thumbnail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp150)
                        .padding(bottom = Dp32)
                        .clip(RoundedCornerShape(Dp12))
                )
                Text(
                    text = stringResource(R.string.choose_file),
                    style = typography.bodyBold1.copy(fontSize = Sp20),
                    color = colorScheme.onPrimary
                )
                forEachIndexed { index, content ->
                    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                        MeverRadioButton(
                            value = if (content.quality.isNotEmpty()) stringResource(
                                R.string.quality, content.quality
                            ) else content.type.ifEmpty { "Video" },
                            isChoosen = chooseQualityIndex == index,
                        ) { chooseQualityIndex = index }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        .onCustomClick { onClickDownload(listContent[chooseQualityIndex].url) }
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