package com.dapascript.mever.feature.home.screen.section

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.component.MeverAutoSizableTextField
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp90
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.feature.home.screen.section.attr.HomeAiSectionAttr.getArtStyles
import com.dapascript.mever.core.common.R as coreUiR

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeAiSection(
    prompt: String,
    totalImageSelected: Int,
    artStyleSelected: String,
    modifier: Modifier = Modifier,
    onPromptChange: (String) -> Unit,
    onImageCountSelected: (Int) -> Unit,
    onArtStyleSelected: (String, String) -> Unit
) = CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
    val context = LocalContext.current
    val imagesCountGenerated = remember { List(4) { it + 1 } }
    val artStyles = remember { getArtStyles(context) }

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = Dp16),
                text = stringResource(coreUiR.string.image_generator),
                style = typography.h2.copy(fontSize = Sp22),
                color = colorScheme.onPrimary
            )
        }
        item {
            Text(
                modifier = Modifier.padding(bottom = Dp24),
                text = stringResource(coreUiR.string.image_generator_desc),
                style = typography.body2,
                color = colorScheme.secondary
            )
        }
        item {
            MeverAutoSizableTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dp150),
                value = prompt,
                fontSize = Sp18,
                minFontSize = Sp14,
                maxLines = 4
            ) { onPromptChange(it) }
        }
        item {
            Text(
                modifier = Modifier.padding(vertical = Dp24),
                text = stringResource(coreUiR.string.total_images),
                style = typography.bodyBold1,
                color = colorScheme.onPrimary
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween
            ) {
                imagesCountGenerated.map { count ->
                    MeverButton(
                        modifier = Modifier
                            .height(Dp40)
                            .width(Dp80),
                        title = count.toString(),
                        buttonType = if (totalImageSelected == count) FILLED else OUTLINED,
                        shape = RoundedCornerShape(Dp12)
                    ) { onImageCountSelected(count) }
                }
            }
        }
        item {
            Row(modifier = Modifier.padding(vertical = Dp24)) {
                Text(
                    text = stringResource(coreUiR.string.art_style),
                    style = typography.bodyBold1,
                    color = colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.size(Dp4))
                Text(
                    text = stringResource(coreUiR.string.optional),
                    style = typography.body2,
                    color = colorScheme.onPrimary
                )
                if (artStyleSelected.isNotEmpty()) Box(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier
                            .align(CenterEnd)
                            .clip(RoundedCornerShape(Dp12))
                            .onCustomClick { onArtStyleSelected("", "") },
                        text = stringResource(coreUiR.string.clear),
                        style = typography.bodyBold2,
                        color = colorScheme.primary
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                artStyles.forEach {
                    val imageSize by animateDpAsState(
                        targetValue = if (artStyleSelected == it.styleName) Dp90 else Dp80
                    )
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = spacedBy(Dp4)
                    ) {
                        Box(
                            modifier = Modifier.size(Dp90),
                            contentAlignment = Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(imageSize)
                                    .clip(RoundedCornerShape(Dp12))
                                    .onCustomClick {
                                        onArtStyleSelected(it.styleName, it.promptKeywords)
                                    },
                                painter = painterResource(it.image),
                                contentDescription = it.styleName
                            )
                        }
                        Text(
                            text = it.styleName,
                            style = typography.bodyBold2,
                            color = colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}