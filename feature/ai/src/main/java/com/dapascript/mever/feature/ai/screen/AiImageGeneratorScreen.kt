package com.dapascript.mever.feature.ai.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverAutoSizableTextField
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp90
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.FadeSide.Bottom
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.fadingEdge
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageGeneratorResultRoute
import com.dapascript.mever.feature.ai.screen.attr.AiImageGeneratorAttr
import com.dapascript.mever.feature.ai.screen.attr.AiImageGeneratorAttr.getArtStyles

@Composable
internal fun AiImageGeneratorScreen(navigator: Navigator) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val deviceType = LocalDeviceType.current
    val listState = rememberLazyListState()
    val showBottomFade by remember {
        derivedStateOf { listState.canScrollForward }
    }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    var rightColumnHeight by remember { mutableStateOf(Dp0) }
    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    var prompt by rememberSaveable { mutableStateOf("") }
    val artStyles = remember(context) { getArtStyles(context) }
    var artStyleSelected by rememberSaveable { mutableStateOf("") }

    val onGenerate = {
        navigator.navigate(
            AiImageGeneratorResultRoute(
                prompt = prompt,
                artStyle = artStyleSelected
            )
        )
    }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded.not()) stringResource(R.string.ai_image_generator) else ""
        ),
        onBackHandler = { navigator.goBack() }
    ) {
        CompositionLocalProvider(LocalOverscrollFactory provides null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp64)
                    .fadingEdge(
                        side = Bottom,
                        isVisible = showBottomFade
                    )
            ) {
                if (isExpanded.not() && titleHeight > 0) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(Dp3),
                        thickness = Dp1,
                        color = colors.blackWhite.copy(alpha = 0.12f)
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = Dp24, end = Dp24),
                    contentPadding = PaddingValues(bottom = if (deviceType == PHONE) Dp64 else Dp90),
                    state = listState
                ) {
                    if (deviceType == PHONE) {
                        item {
                            HeaderSection(
                                isExpanded = isExpanded,
                                onSetTitleHeight = { titleHeight = it }
                            )
                        }
                        item {
                            MeverAutoSizableTextField(
                                heightFreeTextContainer = Dp150,
                                value = prompt,
                                fontSize = Sp18,
                                minFontSize = Sp14,
                                maxLines = 4,
                                onClickInspire = { prompt = getInspirePrompt() },
                                onValueChange = { prompt = it }
                            )
                        }
                        item {
                            ArtStyleHeader(
                                modifier = Modifier.padding(vertical = Dp24),
                                artStyleSelected = artStyleSelected,
                                onClear = { artStyleSelected = "" }
                            )
                        }
                        item {
                            Column(verticalArrangement = spacedBy(Dp16)) {
                                artStyles.chunked(2).forEach { rowItems ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = spacedBy(Dp16)
                                    ) {
                                        rowItems.forEach { item ->
                                            ArtStyleItem(
                                                style = item,
                                                isSelected = artStyleSelected == item.styleName,
                                                onSelect = { name -> artStyleSelected = name },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        if (rowItems.size < 2) Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.size(Dp32))
                            MeverButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dp48),
                                title = stringResource(R.string.generate),
                                buttonType = Filled(
                                    backgroundColor = colors.alwaysPurple,
                                    contentColor = MeverWhite
                                ),
                                isEnabled = prompt.isNotEmpty(),
                                onClick = onGenerate
                            )
                        }
                    } else {
                        item {
                            HeaderSection(
                                isExpanded = isExpanded,
                                onSetTitleHeight = { titleHeight = it }
                            )
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = Dp16),
                                horizontalArrangement = spacedBy(Dp32)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .then(
                                            if (rightColumnHeight > Dp0) Modifier.height(
                                                rightColumnHeight
                                            )
                                            else Modifier
                                        ),
                                    verticalArrangement = spacedBy(Dp24)
                                ) {
                                    MeverAutoSizableTextField(
                                        modifier = Modifier.weight(1f),
                                        value = prompt,
                                        fontSize = Sp18,
                                        minFontSize = Sp14,
                                        maxLines = 4,
                                        onClickInspire = { prompt = getInspirePrompt() },
                                        onValueChange = { prompt = it }
                                    )
                                    MeverButton(
                                        modifier = Modifier
                                            .height(Dp64)
                                            .fillMaxWidth(),
                                        title = stringResource(R.string.generate),
                                        buttonType = Filled(
                                            backgroundColor = colors.alwaysPurple,
                                            contentColor = MeverWhite
                                        ),
                                        isEnabled = prompt.isNotEmpty(),
                                        onClick = onGenerate
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .onGloballyPositioned {
                                            rightColumnHeight =
                                                with(density) { it.size.height.toDp() }
                                        }
                                ) {
                                    ArtStyleHeader(
                                        modifier = Modifier.padding(bottom = Dp24),
                                        artStyleSelected = artStyleSelected,
                                        onClear = { artStyleSelected = "" }
                                    )
                                    Column(verticalArrangement = spacedBy(Dp16)) {
                                        artStyles.chunked(2).forEach { rowItems ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = spacedBy(Dp16)
                                            ) {
                                                rowItems.forEach { item ->
                                                    ArtStyleItem(
                                                        style = item,
                                                        isSelected = artStyleSelected == item.styleName,
                                                        onSelect = { name ->
                                                            artStyleSelected = name
                                                        },
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                                if (rowItems.size < 2) Spacer(
                                                    modifier = Modifier.weight(
                                                        1f
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    isExpanded: Boolean,
    onSetTitleHeight: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = Dp16, bottom = Dp24)
                    .onGloballyPositioned { onSetTitleHeight(it.size.height) },
                text = stringResource(R.string.ai_image_generator),
                style = typography.h2.copy(fontSize = Sp32),
                color = colors.blackWhite
            )
        }
        Text(
            modifier = Modifier.padding(bottom = Dp24),
            text = stringResource(R.string.image_generator_desc),
            style = typography.body2,
            color = colors.grayLightGray
        )
    }
}

@Composable
private fun ArtStyleHeader(
    artStyleSelected: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(R.string.art_style),
            style = typography.bodyBold1,
            color = colors.blackWhite
        )
        Spacer(modifier = Modifier.size(Dp4))
        Text(
            text = stringResource(R.string.optional),
            style = typography.body2,
            color = colors.blackWhite
        )
        if (artStyleSelected.isNotEmpty()) Box(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier
                    .align(CenterEnd)
                    .clip(RoundedCornerShape(Dp12))
                    .onCustomClick { onClear() },
                text = stringResource(R.string.clear),
                style = typography.bodyBold2,
                color = colors.alwaysPurple
            )
        }
    }
}

@Composable
private fun ArtStyleItem(
    style: AiImageGeneratorAttr.StyleOption,
    isSelected: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(Dp4)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dp12))
                .then(
                    if (isSelected) Modifier.border(
                        width = Dp4,
                        color = colors.alwaysPurple,
                        shape = RoundedCornerShape(Dp12)
                    )
                    else Modifier
                )
                .onCustomClick {
                    onSelect(style.styleName)
                },
            painter = painterResource(style.image),
            contentScale = Crop,
            contentDescription = style.styleName
        )
        Text(
            text = style.styleName,
            style = typography.bodyBold3,
            color = colors.blackWhite,
            textAlign = TextAlign.Center
        )
    }
}

private fun getInspirePrompt() = listOf(
    "A lonely robot discovering an ancient forest",
    "A futuristic city floating in the clouds",
    "A dreamy landscape with giant glowing mushrooms",
    "A cinematic moment of two strangers meeting at a station",
    "An astronaut landing in an alien underwater world",
    "An oil painting of life in the 31st century",
    "Flying cats in a pastel-colored sky",
    "A time traveler arrives in a neon-lit future city",
    "A portrait of a woman from a mystical forest kingdom",
    "A child playing among purple-colored clouds",
    "A cyberpunk hero standing in the neon rain",
    "A sunset scene like an indie romance movie",
    "A secret garden hidden in the sky",
    "A post-apocalyptic world in soft pastel colors",
    "A fantasy character walking on a bridge of light",
    "A battle between galactic creatures in a futuristic city",
    "A little girl looking at Earth from the moon",
    "An ancient illustration of a city that never existed",
    "A light festival in a floating village",
    "A surreal painting of an endless dream"
).random()