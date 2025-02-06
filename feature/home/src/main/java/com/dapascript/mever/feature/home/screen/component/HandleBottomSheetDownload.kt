package com.dapascript.mever.feature.home.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverLocalThumbnail
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
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.isValidUrl
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.model.local.ContentEntity

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
        onClickDismiss = onClickDismiss
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Dp24)
        ) {
            val context = LocalContext.current
            var chooseQuality by remember(this) { mutableStateOf(firstOrNull()?.url) }

            if (get(0).thumbnail.isEmpty()) MeverUrlThumbnail(
                source = size.takeIf { it > 0 }?.let { get(0).url } ?: "",
                isFailedFetchImage = isFailedFetchImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dp150)
                    .padding(bottom = Dp32)
                    .clip(RoundedCornerShape(Dp12))
            ) else MeverLocalThumbnail(
                source = get(0).thumbnail,
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
            AnimatedVisibility(takeIf { (all { it.quality.isEmpty() }) } != null) {
                var contentType by remember(this) { mutableStateOf(context.getString(R.string.wait)) }
                LaunchedEffect(get(0).url) {
                    contentType = getUrlContentType(get(0).url)?.let { type ->
                        if (type.isVideo()) context.getString(R.string.video_text)
                        else context.getString(R.string.image_text)
                    }.orEmpty()
                }
                RadioButtonSection(
                    url = get(0).url,
                    quality = contentType,
                    chooseQuality = chooseQuality ?: get(0).url
                ) { chooseQuality = it }
            }
            filter { it.url.isValidUrl() && it.quality.isNotEmpty() }.forEach { (url, quality) ->
                RadioButtonSection(
                    url = url,
                    quality = stringResource(R.string.quality, quality),
                    chooseQuality = chooseQuality ?: get(0).url
                ) { chooseQuality = it }
            }
            Row(
                modifier = Modifier.height(Min),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dp14))
                        .clickableSingle { onClickDismiss() }
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
                        .clickableSingle { onClickDownload(chooseQuality ?: get(0).url) }
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
private fun RadioButtonSection(
    url: String,
    quality: String,
    chooseQuality: String,
    onChooseQuality: (String) -> Unit
) = CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dp16)
            .clip(RoundedCornerShape(Dp8))
            .clickableSingle { onChooseQuality(url) },
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(Dp16)
    ) {
        MeverRadioButton(isChecked = chooseQuality == url) { onChooseQuality(url) }
        Text(
            text = quality,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}