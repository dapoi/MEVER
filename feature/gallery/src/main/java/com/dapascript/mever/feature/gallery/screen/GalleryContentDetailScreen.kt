package com.dapascript.mever.feature.gallery.screen

import android.annotation.SuppressLint
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.SystemBarStyle.Companion.light
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.lerp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.component.MeverDeclinedPermission
import com.dapascript.mever.core.common.ui.component.MeverDialogError
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.MeverPhotoViewer
import com.dapascript.mever.core.common.ui.component.MeverVideoPlayer
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.convertFilename
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.handleClickButton
import com.dapascript.mever.core.common.util.sanitizeFilename
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.storage.StorageUtil.getStorageInfo
import com.dapascript.mever.core.common.util.storage.StorageUtil.isStorageFull
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.gallery.viewmodel.GalleryContentDetailViewModel
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.absoluteValue

@SuppressLint("FrequentlyChangingValue")
@OptIn(UnstableApi::class)
@Composable
internal fun GalleryContentDetailScreen(
    navController: NavController,
    viewModel: GalleryContentDetailViewModel = hiltViewModel()
) = with(viewModel) {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var imageExploreData by remember { mutableStateOf(Pair("", "")) }
    var setStoragePermission by remember { mutableStateOf<List<String>>(emptyList()) }
    val pagerState = rememberPagerState(args.initialIndex) { args.contents.size }
    val context = LocalContext.current
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val themeType = themeType.collectAsStateValue()
    val isPipEnabled = isPipEnabled.collectAsStateValue()
    val getButtonClickCount = getButtonClickCount.collectAsStateValue()
    val scope = rememberCoroutineScope()
    val interstitialAd = rememberInterstitialAd { setStoragePermission = getStoragePermission() }
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

        MeverDialogError(
            showDialog = errorMessage.isNotEmpty(),
            errorTitle = stringResource(R.string.error_title),
            errorDescription = errorMessage,
            primaryButtonText = stringResource(R.string.ok),
            onClickPrimary = {
                errorMessage = ""
                scope.launch {
                    startDownload(imageExploreData.first, imageExploreData.second)
                    val nextPage = pagerState.currentPage + 1
                    if (nextPage < pagerState.pageCount) {
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            },
            onClickSecondary = { errorMessage = "" }
        )

        if (setStoragePermission.isNotEmpty()) {
            val storageInfo = remember { getStorageInfo(context) }
            MeverPermissionHandler(
                permissions = setStoragePermission,
                onGranted = {
                    setStoragePermission = emptyList()
                    if (isStorageFull(storageInfo)) {
                        errorMessage = resources.getString(R.string.storage_full)
                    } else {
                        startDownload(imageExploreData.first, imageExploreData.second)
                        navController.navigateTo(
                            route = GalleryLandingRoute,
                            popUpTo = GalleryContentDetailRoute::class,
                            inclusive = true
                        )
                        imageExploreData = Pair("", "")
                    }
                },
                onDenied = { isPermanentlyDeclined, retry ->
                    MeverDeclinedPermission(
                        isPermissionsDeclined = isPermanentlyDeclined,
                        onGoToSetting = {
                            setStoragePermission = emptyList()
                            activity.goToSetting()
                        },
                        onRetry = { retry() },
                        onDismiss = { setStoragePermission = emptyList() }
                    )
                }
            )
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
                if (isVideo) MeverVideoPlayer(
                    modifier = itemModifier,
                    fileName = convertFilename(fileName),
                    videoSource = primaryContent,
                    isPreview = isPreview,
                    isPageVisible = pagerState.currentPage == page,
                    isScrolling = pagerState.isScrollInProgress,
                    isAutoplayTarget = args.initialIndex == page || args.contents[page].isPreview,
                    isFullScreen = isFullScreen,
                    isPipEnabled = isPipEnabled,
                    onFullScreenChange = { isFullScreen = it },
                    onClickDelete = { deleteContent(id) },
                    onClickShare = {
                        shareContent(
                            context = context,
                            file = File(primaryContent)
                        )
                    },
                    onClickBack = { navController.popBackStack() }
                ) else MeverPhotoViewer(
                    modifier = itemModifier,
                    fileName = convertFilename(fileName),
                    isDownloadable = isDownloadable,
                    isPreview = isPreview,
                    primaryImage = primaryContent,
                    onClickDelete = { deleteContent(id) },
                    onClickShare = {
                        shareContent(
                            context = context,
                            file = File(primaryContent)
                        )
                    },
                    onClickBack = { navController.popBackStack() },
                    onClickDownload = { url, filename ->
                        handleClickButton(
                            buttonClickCount = getButtonClickCount,
                            onIncrementClickCount = {
                                incrementClickCount()
                                imageExploreData = Pair(url, sanitizeFilename(filename))
                            },
                            onShowAds = { interstitialAd.showAd() },
                            onClickAction = { setStoragePermission = getStoragePermission() }
                        )
                    }
                )
            }
        }
    }
}