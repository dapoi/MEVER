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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
internal fun HandleBottomSheetYouTubeQuality(
    showBottomSheet: Boolean,
    qualityList: List<String>,
    modifier: Modifier = Modifier,
    onApplyQuality: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var chooseQuality by remember(qualityList) {
        mutableStateOf(if (qualityList.isNotEmpty()) qualityList.first() else "")
    }
    val scrollState = rememberScrollState()

    MeverBottomSheet(
        modifier = modifier,
        showBottomSheet = showBottomSheet,
        shouldDismissOnClickOutside = true,
        skipPartiallyExpanded = (scrollState.canScrollForward || scrollState.canScrollBackward).not(),
        onDismissBottomSheet = onDismiss
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .verticalScroll(scrollState),
            verticalArrangement = spacedBy(Dp8)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.choose_quality),
                textAlign = TextAlign.Center,
                style = typography.bodyBold1.copy(fontSize = Sp18),
                color = colorScheme.onPrimary
            )
            qualityList.map { quality ->
                MeverRadioButton(
                    value = getQualityContent(quality),
                    isChoosen = chooseQuality == quality
                ) { chooseQuality = quality }
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
                        .onCustomClick { onDismiss() }
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
                        .onCustomClick { onApplyQuality(chooseQuality) }
                        .weight(1f)
                        .padding(vertical = Dp16),
                    contentAlignment = Center
                ) {
                    Text(
                        text = stringResource(R.string.apply),
                        style = typography.bodyBold1,
                        color = colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun getQualityContent(quality: String) = if (quality.contains("kbps")) {
    "Audio - ${quality.replace("kbps", "Kbps")}"
} else "Video - $quality"