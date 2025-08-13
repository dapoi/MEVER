package com.dapascript.mever.feature.gallery.screen

import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.SystemBarStyle.Companion.light
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.component.MeverPhotoViewer
import com.dapascript.mever.core.common.ui.component.MeverVideoPlayer
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.feature.gallery.viewmodel.GalleryPlayerViewModel
import java.io.File
import kotlin.math.absoluteValue

@Composable
internal fun GalleryContentDetailScreen(
    navigator: NavController,
    viewModel: GalleryPlayerViewModel = hiltViewModel()
) = with(viewModel) {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    val pagerState = rememberPagerState(args.initialIndex) { args.contents.size }
    val context = LocalContext.current
    val activity = LocalActivity.current
    val themeType = themeType.collectAsStateValue()
    val darkTheme = when (themeType) {
        Light -> false
        Dark -> true
        else -> isSystemInDarkTheme()
    }

    BaseScreen(
        useSystemBarsPadding = false,
        allowScreenOverlap = true,
        hideDefaultTopBar = true,
        lockOrientation = false
    ) {
        DisposableEffect(darkTheme) {
            activity.enableEdgeToEdge(
                statusBarStyle = dark(scrim = MeverTransparent.toArgb()),
                navigationBarStyle = dark(scrim = MeverTransparent.toArgb())
            )
            onDispose {
                activity.enableEdgeToEdge(
                    statusBarStyle = if (darkTheme) {
                        dark(scrim = MeverDark.toArgb())
                    } else {
                        light(
                            scrim = MeverTransparent.toArgb(),
                            darkScrim = MeverDark.toArgb()
                        )
                    },
                    navigationBarStyle = if (darkTheme) {
                        dark(scrim = MeverDark.toArgb())
                    } else {
                        light(
                            scrim = MeverTransparent.toArgb(),
                            darkScrim = MeverDark.toArgb()
                        )
                    }
                )
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(MeverBlack),
            state = pagerState,
            userScrollEnabled = isFullScreen.not(),
            beyondViewportPageCount = 1,
            key = { page -> args.contents[page].id }
        ) { page ->
            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
            val itemModifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val scale = lerp(
                        start = 1f,
                        stop = 0.85f,
                        fraction = pageOffset.coerceIn(0f, 1f)
                    )
                    scaleX = scale
                    scaleY = scale
                    alpha = lerp(
                        start = 1f,
                        stop = 0.5f,
                        fraction = pageOffset.coerceIn(0f, 1f)
                    )
                }
            val content = args.contents[page]

            with(content) {
                if (isVideo(filePath)) MeverVideoPlayer(
                    modifier = itemModifier,
                    source = filePath,
                    isScrolling = pagerState.isScrollInProgress,
                    isPageVisible = pagerState.currentPage == page,
                    isInitialIndex = args.initialIndex == page,
                    isFullScreen = isFullScreen,
                    onFullScreenChange = { isFullScreen = it },
                    onClickDelete = { deleteContent(id) },
                    onClickShare = {
                        shareContent(
                            context = context,
                            file = File(filePath)
                        )
                    },
                    onClickBack = { navigator.popBackStack() }
                ) else MeverPhotoViewer(
                    modifier = itemModifier,
                    source = filePath,
                    onClickDelete = { deleteContent(id) },
                    onClickShare = {
                        shareContent(
                            context = context,
                            file = File(filePath)
                        )
                    },
                    onClickBack = { navigator.popBackStack() }
                )
            }
        }
    }
}