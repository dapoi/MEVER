package com.dapascript.mever.feature.startup.screen

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.ContentScale.Companion.FillHeight
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.MeverYellow
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp19
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp36
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp50
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.DESKTOP
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.formatHighlightedText
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.feature.startup.screen.attr.OnboardScreenAttr.OnboardPage
import com.dapascript.mever.feature.startup.viewmodel.OnboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun OnboardScreen(
    navigator: Navigator,
    viewModel: OnboardViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        hideDefaultTopBar = true,
        useStatusBarsPadding = false,
        onBackHandler = { navigator.goBack() }
    ) {
        var setRequestPermission by remember { mutableStateOf<List<String>>(emptyList()) }
        val activity = LocalActivity.current
        val context = LocalContext.current
        val deviceType = LocalDeviceType.current
        val pagerState = rememberPagerState(pageCount = { pages.size })
        val scope = rememberCoroutineScope()

        if (setRequestPermission.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = setRequestPermission,
                onGranted = {
                    setRequestPermission = emptyList()
                    navigator.navigateToHome()
                },
                onDenied = { _, _ ->
                    setRequestPermission = emptyList()
                    navigator.navigateToHome()
                }
            )
        }

        BackHandler {
            if (pagerState.currentPage > 0) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            } else activity.finish()
        }

        CompositionLocalProvider(LocalOverscrollFactory provides null) {
            if (deviceType == PHONE) {
                OnboardPhoneContent(
                    pages = pages,
                    pagerState = pagerState,
                    scope = scope,
                    onClickLaunch = {
                        setIsOnboarded(true)
                        val perm = getNotificationPermission().firstOrNull()
                        if (perm != null && context.checkSelfPermission(perm) != PERMISSION_GRANTED) {
                            setRequestPermission = listOf(perm)
                        } else navigator.navigateToHome()
                    }
                )
            } else {
                OnboardTabletContent(
                    pages = pages,
                    pagerState = pagerState,
                    scope = scope,
                    deviceType = deviceType,
                    onClickLaunch = {
                        setIsOnboarded(true)
                        val perm = getNotificationPermission().firstOrNull()
                        if (perm != null && context.checkSelfPermission(perm) != PERMISSION_GRANTED) {
                            setRequestPermission = listOf(perm)
                        } else navigator.navigateToHome()
                    }
                )
            }
        }
    }
}

@Composable
private fun OnboardPhoneContent(
    pages: List<OnboardPage>,
    pagerState: PagerState,
    scope: CoroutineScope,
    onClickLaunch: () -> Unit
) {
    var buttonSize by remember { mutableStateOf(Dp0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.whiteBlack)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    modifier = Modifier
                        .aspectRatio(0.9f)
                        .padding(top = Dp64),
                    painter = painterResource(pages[page].image),
                    contentScale = FillHeight,
                    contentDescription = "Onboard Image ${page + 1}"
                )
                DescriptionOnboardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dp24, bottom = buttonSize, start = Dp24, end = Dp24),
                    page = pages[page]
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colors.whiteBlack)
                .padding(start = Dp24, end = Dp24, bottom = Dp16)
                .navigationBarsPadding()
                .align(BottomCenter)
                .onGloballyPositioned { buttonSize = it.size.height.dp }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dp24),
                horizontalArrangement = Center
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val size by animateDpAsState(
                        targetValue = if (isSelected) Dp24 else Dp8,
                        animationSpec = tween(durationMillis = 300),
                        label = "indicator_size"
                    )
                    val alpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.3f,
                        animationSpec = tween(durationMillis = 300),
                        label = "indicator_alpha"
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = Dp6)
                            .size(width = size, height = Dp8)
                            .clip(RoundedCornerShape(Dp48))
                            .background(
                                if (isSelected) MeverPurple
                                else colors.grayLightGray.copy(alpha = alpha)
                            )
                    )
                }
            }
            ButtonOnboardSection(
                isLastPage = pagerState.currentPage == pages.size - 1,
                onClickNext = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onClickLaunch()
                    }
                },
                onClickLaunch = onClickLaunch
            )
        }
    }
}

@Composable
private fun OnboardTabletContent(
    pages: List<OnboardPage>,
    pagerState: PagerState,
    scope: CoroutineScope,
    deviceType: DeviceType,
    onClickLaunch: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.whiteBlack)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(pages[page].image),
                        contentScale = if (deviceType == DESKTOP) Crop else Fit,
                        contentDescription = "Onboard Image ${page + 1}"
                    )
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(MeverTransparent, colors.whiteBlack),
                                startX = 100f,
                                endX = 1000f
                            )
                        )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colors.whiteBlack)
                    .weight(1f)
                    .padding(top = Dp40, bottom = Dp24, start = Dp16, end = Dp16),
                verticalArrangement = Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                DescriptionOnboardSection(
                    modifier = Modifier.fillMaxWidth(),
                    page = pages[pagerState.currentPage]
                )
                Spacer(modifier = Modifier.size(Dp40))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dp24),
                    horizontalArrangement = Center
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val size by animateDpAsState(
                            targetValue = if (isSelected) Dp24 else Dp8,
                            animationSpec = tween(durationMillis = 300),
                            label = "indicator_size"
                        )
                        val alpha by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.3f,
                            animationSpec = tween(durationMillis = 300),
                            label = "indicator_alpha"
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = Dp6)
                                .size(width = size, height = Dp8)
                                .clip(RoundedCornerShape(Dp48))
                                .background(
                                    if (isSelected) MeverPurple
                                    else colors.grayLightGray.copy(alpha = alpha)
                                )
                        )
                    }
                }
                ButtonOnboardSection(
                    isLastPage = pagerState.currentPage == pages.size - 1,
                    onClickNext = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onClickLaunch()
                        }
                    },
                    onClickLaunch = onClickLaunch
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DescriptionOnboardSection(
    page: OnboardPage,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    Text(
        text = page.subtitle,
        style = typography.body2,
        color = colors.grayLightGray
    )
    Spacer(modifier = Modifier.size(Dp8))
    Text(
        text = formatHighlightedText(
            fullText = page.title,
            highlightedText = page.highlightedText,
            highlightedColor = colors.alwaysPurple
        ),
        color = colors.blackWhite,
        style = typography.h2.copy(fontSize = Sp36, lineHeight = Sp50)
    )
    Spacer(modifier = Modifier.size(Dp8))
    Text(
        text = page.description,
        style = typography.body2,
        color = colors.grayLightGray
    )
}

@Composable
private fun ButtonOnboardSection(
    isLastPage: Boolean,
    onClickNext: () -> Unit,
    onClickLaunch: () -> Unit
) = MeverButton(
    modifier = Modifier
        .fillMaxWidth()
        .height(Dp52),
    title = if (isLastPage) stringResource(R.string.lets_start)
    else stringResource(R.string.next),
    buttonType = Filled(
        backgroundColor = MeverPurple,
        contentColor = MeverWhite
    ),
    shape = RoundedCornerShape(Dp48),
    textSize = Sp19,
    trailingIcon = painterResource(R.drawable.ic_arrow_started),
    trailingIconTint = MeverYellow,
    onClick = if (isLastPage) onClickLaunch else onClickNext
)

private fun Navigator.navigateToHome() {
    navigate(
        route = HomeLandingRoute,
        popUpTo = OnboardRoute,
        isInclusive = true
    )
}