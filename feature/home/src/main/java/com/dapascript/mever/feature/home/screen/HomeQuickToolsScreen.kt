package com.dapascript.mever.feature.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverFeatureCard
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.AI_IMAGE
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.FIND_IMAGE
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getCardColor
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getRoute
import com.dapascript.mever.feature.home.viewmodel.HomeQuickToolsViewModel

@Composable
internal fun HomeQuickToolsScreen(
    navigator: Navigator,
    args: HomeQuickToolsRoute,
    viewModel: HomeQuickToolsViewModel = hiltViewModel()
) = with(viewModel) {
    val isImageAiEnabled = isImageAiEnabled.collectAsStateValue()
    val isGoImgEnabled = isGoImgEnabled.collectAsStateValue()
    val filteredFeatureCards = remember(args.featureCards, isImageAiEnabled, isGoImgEnabled) {
        args.featureCards.filter { data ->
            when (data.toolsType) {
                AI_IMAGE -> isImageAiEnabled
                FIND_IMAGE -> isGoImgEnabled
                else -> true
            }
        }
    }
    val listState = rememberLazyListState()
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(title = if (isExpanded.not()) stringResource(R.string.quick_tools) else ""),
        onBackHandler = { navigator.goBack() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp64)
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
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = Dp189),
                    state = listState
                ) {
                    item {
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Text(
                                text = stringResource(R.string.quick_tools),
                                style = typography.h2.copy(fontSize = Sp32),
                                color = colors.blackWhite,
                                modifier = Modifier
                                    .padding(
                                        top = Dp16,
                                        start = Dp24,
                                        end = Dp24,
                                        bottom = Dp24
                                    )
                                    .onGloballyPositioned { titleHeight = it.size.height }
                            )
                        }
                    }

                    items(filteredFeatureCards.toList()) { data ->
                        MeverFeatureCard(
                            modifier = Modifier.padding(
                                start = Dp24,
                                end = Dp24,
                                bottom = Dp16
                            ),
                            icon = data.icon,
                            title = data.featureName,
                            desc = data.featureDesc,
                            cardColor = data.toolsType.getCardColor(),
                            titleStyle = typography.bodyBold2,
                            descStyle = typography.body3
                        ) { navigator.navigate(data.toolsType.getRoute()) }
                    }
                }
            }
            MeverBannerAd(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}