package com.dapascript.mever.feature.gallery.screen

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.sanitizeFilename
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.storage.StorageUtil.getStorageInfo
import com.dapascript.mever.core.common.util.storage.StorageUtil.isStorageFull
import com.dapascript.mever.feature.gallery.viewmodel.GalleryContentDetailViewModel
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.absoluteValue

@OptIn(UnstableApi::class)
@Composable
internal fun GalleryContentDetailScreen(
    navigator: NavController,
    viewModel: GalleryContentDetailViewModel = hiltViewModel()
) = with(viewModel) {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var imageExploreData by remember { mutableStateOf(Pair("", "")) }
    var setStoragePermission by remember { mutableStateOf<List<String>>(emptyList()) }
    val pagerState = rememberPagerState(args.initialIndex) { args.contents.size }
    val context = LocalContext.current
    val activity = LocalActivity.current
    val themeType = themeType.collectAsStateValue()
    val isPipEnabled = isPipEnabled.collectAsStateValue()
    val scope = rememberCoroutineScope()
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
                Toast.makeText(
                    context,
                    context.getString(R.string.image_has_been_downloaded),
                    LENGTH_SHORT
                ).show()
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
                        errorMessage = context.getString(R.string.storage_full)
                    } else {
                        scope.launch {
                            startDownload(imageExploreData.first, imageExploreData.second)
                            val nextPage = pagerState.currentPage + 1
                            if (nextPage < pagerState.pageCount) {
                                pagerState.animateScrollToPage(nextPage)
                            }
                        }
                        Toast.makeText(
                            context,
                            context.getString(R.string.image_has_been_downloaded),
                            LENGTH_SHORT
                        ).show()
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
                    fileName = fileName,
                    videoSource = primaryContent,
                    isOnlineContent = isOnlineContent,
                    isPageVisible = pagerState.currentPage == page,
                    isScrolling = pagerState.isScrollInProgress,
                    isAutoplayTarget = args.initialIndex == page || args.contents[page].isOnlineContent,
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
                    onClickBack = { navigator.popBackStack() }
                ) else MeverPhotoViewer(
                    modifier = itemModifier,
                    fileName = fileName,
                    isOnlineContent = isOnlineContent,
                    primaryImage = primaryContent,
                    secondaryImage = secondaryContent,
                    onClickDelete = { deleteContent(id) },
                    onClickShare = {
                        shareContent(
                            context = context,
                            file = File(primaryContent)
                        )
                    },
                    onClickBack = { navigator.popBackStack() },
                    onClickDownload = { url, filename ->
                        setStoragePermission = getStoragePermission()
                        imageExploreData = Pair(url, sanitizeFilename(filename))
                    }
                )
            }
        }
    }
}