package com.dapascript.mever.feature.setting.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.feature.setting.screen.attr.SettingFaqAttr.FaqUiModel
import com.dapascript.mever.feature.setting.viewmodel.SettingFaqViewModel

@Composable
internal fun SettingFaqScreen(
    navigator: Navigator,
    viewModel: SettingFaqViewModel = hiltViewModel()
) = with(viewModel) {
    val listState = rememberLazyListState()
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    val faqList = faqList.collectAsStateValue()

    BaseScreen(
        topBarArgs = TopBarArgs(title = if (isExpanded.not()) stringResource(R.string.faq) else ""),
        onBackHandler = { navigator.goBack() }
    ) {
        SettingFaqContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64),
            isExpanded = isExpanded,
            listState = listState,
            faqList = faqList,
            onExpand = { onExpand(it) },
            onSetTitleHeight = { titleHeight = it }
        )
    }
}

@Composable
private fun SettingFaqContent(
    isExpanded: Boolean,
    listState: LazyListState,
    faqList: List<FaqUiModel>,
    onExpand: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onSetTitleHeight: (Int) -> Unit
) {
    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        Box(modifier = modifier) {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.faq),
                        style = typography.h2.copy(fontSize = Sp32),
                        color = colors.blackWhite,
                        modifier = Modifier
                            .padding(
                                top = Dp16,
                                start = Dp24,
                                end = Dp24,
                                bottom = Dp24
                            )
                            .onGloballyPositioned { onSetTitleHeight(it.size.height) }
                    )
                }
                if (isExpanded.not()) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(Dp3),
                        thickness = Dp1,
                        color = colors.blackWhite.copy(alpha = 0.12f)
                    )
                }
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = listState,
                    contentPadding = PaddingValues(
                        start = Dp24,
                        end = Dp24,
                        bottom = Dp150
                    )
                ) {
                    items(faqList, key = { it.id }) { faq ->
                        SettingFaqItem(
                            title = stringResource(faq.question),
                            description = stringResource(faq.answer),
                            isDescriptionVisible = faq.isExpanded,
                            isLink = faq.isLink,
                            onClick = { onExpand(faq.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingFaqItem(
    title: String,
    description: String,
    isDescriptionVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLink: Boolean = false
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dp20)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = typography.bodyBold1,
                color = colors.blackWhite
            )
            AnimatedVisibility(
                visible = isDescriptionVisible,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                val annotatedString = if (isLink) {
                    val linkText = "github.com/dapoi/MEVER"
                    val startIndex = description.indexOf(linkText)
                    if (startIndex != -1) {
                        buildAnnotatedString {
                            append(description.substring(0, startIndex))
                            pushStringAnnotation(tag = "URL", annotation = "https://$linkText")
                            withStyle(
                                style = SpanStyle(
                                    color = colors.alwaysPurple,
                                    fontStyle = FontStyle.Italic,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(linkText)
                            }
                            pop()
                            append(description.substring(startIndex + linkText.length))
                        }
                    } else {
                        buildAnnotatedString { append(description) }
                    }
                } else {
                    buildAnnotatedString { append(description) }
                }

                var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

                Text(
                    text = annotatedString,
                    style = typography.body1,
                    color = colors.blackWhite.copy(alpha = 0.7f),
                    onTextLayout = { textLayoutResult = it },
                    modifier = Modifier
                        .padding(top = Dp3)
                        .pointerInput(isLink) {
                            detectTapGestures { offset ->
                                if (isLink) {
                                    textLayoutResult?.let { layout ->
                                        val position = layout.getOffsetForPosition(offset)
                                        val annotation = annotatedString
                                            .getStringAnnotations("URL", position, position)
                                            .firstOrNull()

                                        if (annotation != null) {
                                            uriHandler.openUri(annotation.item)
                                        } else {
                                            onClick()
                                        }
                                    }
                                } else {
                                    onClick()
                                }
                            }
                        }
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_back),
            tint = colors.blackWhite.copy(alpha = 0.6f),
            contentDescription = "Icon FAQ",
            modifier = Modifier
                .size(Dp16)
                .graphicsLayer {
                    rotationZ = if (isDescriptionVisible) 90f else -90f
                }
        )
    }
}
