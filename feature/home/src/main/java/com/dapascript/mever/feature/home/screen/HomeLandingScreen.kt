package com.dapascript.mever.feature.home.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.GalleryNavGraph
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverPlatformIcon
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.component.MeverTabs
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.MeverThumbnail
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp18
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.Constant.PlatformName.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.getDescriptionPermission
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.getNetworkStatus
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.isValidUrl
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.DownloaderArgs
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.listOfActionMenu
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel
import com.ketch.DownloadModel
import com.ketch.Status.PAUSED
import kotlinx.coroutines.launch

@Composable
internal fun HomeLandingScreen(
    navigator: BaseNavigator,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val isNetworkAvailable = connectivityObserver.observe().collectAsState(connectivityObserver.isConnected())
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    var listVideo by remember { mutableStateOf<List<VideoGeneralEntity>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }
    var showErrorNetworkModal by remember { mutableStateOf(false) }
    var showErrorResponseModal by remember { mutableStateOf<Throwable?>(null) }
    val onClickActionMenu = remember { handleClickActionMenu(navigator) }
    val requestStoragePermissionLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) { perms ->
        val allGranted = getStoragePermission.all { perms[it] == true }

        if (allGranted) getNetworkStatus(
            isNetworkAvailable = isNetworkAvailable.value,
            onNetworkAvailable = { getApiDownloader(urlSocialMediaState) },
            onNetworkUnavailable = { showErrorNetworkModal = true }
        ) else getStoragePermission.forEach { permission ->
            onPermissionResult(permission, isGranted = perms[permission] == true)
        }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(
            screenName = null,
            actionMenus = listOfActionMenu.map { (name, resource) ->
                ActionMenu(
                    icon = resource,
                    nameIcon = name,
                    showBadge = showBadge && name == NOTIFICATION,
                ) { onClickActionMenu(name) }
            }
        )
    ) {
        LaunchedEffect(Unit) { getObservableKetch() }
        LaunchedEffect(videoState) {
            videoState.handleUiState(
                onLoading = { showLoading = true },
                onSuccess = {
                    showLoading = false
                    listVideo = it
                },
                onFailed = {
                    showLoading = false
                    showErrorResponseModal = it
                }
            )
        }

        HandlerDialogError(
            showDialog = showErrorNetworkModal,
            errorTitle = "Ups, You're Offline",
            errorImage = R.drawable.ic_offline,
            errorDescription = "Please check your internet connection and try again.",
            onRetry = {
                showErrorNetworkModal = false
                getNetworkStatus(
                    isNetworkAvailable = isNetworkAvailable.value,
                    onNetworkAvailable = { getApiDownloader(urlSocialMediaState) },
                    onNetworkUnavailable = { showErrorNetworkModal = true }
                )
            },
            onDismiss = { showErrorNetworkModal = false }
        )

        HandlerDialogError(
            showDialog = showErrorResponseModal != null,
            errorTitle = "Something Went Wrong!",
            errorImage = R.drawable.ic_error_response,
            errorDescription = "Your request cannot be processed at this time. Please try again later.",
            onRetry = {
                showErrorResponseModal = null
                getNetworkStatus(
                    isNetworkAvailable = isNetworkAvailable.value,
                    onNetworkAvailable = { getApiDownloader(urlSocialMediaState) },
                    onNetworkUnavailable = { showErrorNetworkModal = true }
                )
            },
            onDismiss = { showErrorResponseModal = null }
        )

        HandleDialogPermission(
            activity = activity,
            dialogQueue = dialogQueue,
            onGoToSetting = {
                dismissDialog()
                activity.goToSetting()
            },
            onAllow = {
                dismissDialog()
                requestStoragePermissionLauncher.launch(getStoragePermission)
            },
            onDismiss = ::dismissDialog
        )

        HandleDialogDownload(
            downloadArgs = listVideo.map {
                DownloaderArgs(
                    url = it.url,
                    quality = it.quality
                )
            },
            showDialog = listVideo.isNotEmpty(),
            onClickDownload = { url ->
                downloadFile(
                    url = url,
                    platformName = urlSocialMediaState.text.getPlatformType().platformName
                )
                listVideo = emptyList()
                urlSocialMediaState = urlSocialMediaState.copy(text = "")
            },
            onDismiss = { listVideo = emptyList() }
        )

        HomeScreenContent(
            homeLandingViewModel = this,
            navigator = navigator,
            isLoading = showLoading
        ) { requestStoragePermissionLauncher.launch(getStoragePermission) }
    }
}


@Composable
private fun HandlerDialogError(
    showDialog: Boolean,
    errorTitle: String,
    errorImage: Int,
    errorDescription: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = errorTitle,
            primaryButtonText = "Try Again",
            onClickAction = onRetry,
            onDismiss = onDismiss
        )
    ) {
        Image(
            modifier = Modifier
                .size(Dp200)
                .align(CenterHorizontally),
            painter = painterResource(errorImage),
            contentScale = Crop,
            contentDescription = "Error Image"
        )
        Text(
            text = errorDescription,
            textAlign = Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}

@Composable
private fun HandleDialogPermission(
    activity: Activity,
    dialogQueue: List<String>,
    onGoToSetting: () -> Unit,
    onAllow: () -> Unit,
    onDismiss: () -> Unit
) {
    dialogQueue.reversed().forEach { permission ->
        val isPermissionsDeclined = shouldShowRequestPermissionRationale(activity, permission).not()

        MeverDialog(
            showDialog = true,
            meverDialogArgs = MeverDialogArgs(
                title = "Permission Required",
                primaryButtonText = if (isPermissionsDeclined) "Go to setting" else "Allow",
                onClickAction = if (isPermissionsDeclined) onGoToSetting else onAllow,
                onDismiss = onDismiss
            )
        ) {
            Text(
                text = getDescriptionPermission(permission),
                textAlign = Center,
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun HandleDialogDownload(
    downloadArgs: List<DownloaderArgs>,
    showDialog: Boolean,
    onClickDownload: (String) -> Unit,
    onDismiss: () -> Unit
) = with(downloadArgs) {
    var chooseQuality by remember(this) { mutableStateOf(firstOrNull { it.quality.isNotEmpty() }?.url) }

    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = "Content's Found",
            primaryButtonText = "Download",
            onClickAction = { onClickDownload(chooseQuality ?: get(0).url) },
            onDismiss = onDismiss
        )
    ) {
        MeverThumbnail(
            source = size.takeIf { it > 0 }?.let { get(0).url } ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(Dp8))
        )
        filter { it.quality.isNotEmpty() && it.url.isValidUrl() }.map { (url, quality) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dp4))
                    .clickableSingle { chooseQuality = url },
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = quality,
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
                MeverRadioButton(isChecked = chooseQuality == url) { chooseQuality = url }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    homeLandingViewModel: HomeLandingViewModel,
    navigator: BaseNavigator,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    requestStoragePermissionLauncher: () -> Unit
) = with(homeLandingViewModel) {
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }

    showDeleteDialog?.let { id ->
        MeverDialog(
            showDialog = true,
            meverDialogArgs = MeverDialogArgs(
                title = "Delete this file?",
                primaryButtonText = "Delete",
                onClickAction = {
                    ketch.clearDb(id)
                    showDeleteDialog = null
                },
                onDismiss = { showDeleteDialog = null }
            )
        ) {
            Text(
                text = "File that has been deleted cannot be recovered",
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = Dp18)
    ) {
        BoxWithConstraints {
            val screenHeight = maxHeight
            val context = LocalContext.current
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dp24)
            ) {
                Column(
                    modifier = Modifier.height(screenHeight),
                    horizontalAlignment = CenterHorizontally
                ) {
                    MeverTabs(
                        items = tabItems,
                        pagerState = pagerState
                    ) { scope.launch { pagerState.animateScrollToPage(it) } }
                    Spacer(modifier = Modifier.size(Dp24))
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .nestedScroll(remember {
                                object : NestedScrollConnection {
                                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                        return if (available.y > 0) Offset.Zero
                                        else Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                                    }
                                }
                            })
                    ) { index ->
                        when (index) {
                            0 -> HomeVideoSection(
                                downloadList = downloadList,
                                isLoading = isLoading,
                                urlSocialMediaState = urlSocialMediaState,
                                onClickDownloading = { if (it.status == PAUSED) ketch.resume(it.id) else ketch.pause(it.id) },
                                onClickDelete = { showDeleteDialog = it.id },
                                onClickShare = {
                                    shareContent(
                                        context = context,
                                        authority = context.packageName,
                                        path = getMeverFiles()?.find { file ->
                                            file.name == it.fileName
                                        }?.path.orEmpty()
                                    )
                                },
                                onClickPlay = {
                                    with(it) {
                                        navigator.run {
                                            navigate(
                                                getNavGraph<GalleryNavGraph>().getGalleryContentViewerRoute(
                                                    id = id,
                                                    sourceFile = getMeverFiles()?.find { file ->
                                                        file.name == fileName
                                                    }?.path.orEmpty(),
                                                    fileName = fileName.replaceTimeFormat()
                                                )
                                            )
                                        }
                                    }
                                },
                                onValueChange = { urlSocialMediaState = it },
                                onClickDownload = { if (isLoading.not()) requestStoragePermissionLauncher() },
                                onClickViewAll = { navigator.navigateToGalleryScreen() }
                            )

                            1 -> {}
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeVideoSection(
    downloadList: List<DownloadModel>,
    isLoading: Boolean,
    urlSocialMediaState: TextFieldValue,
    modifier: Modifier = Modifier,
    onClickDownloading: (DownloadModel) -> Unit,
    onClickDelete: (DownloadModel) -> Unit,
    onClickShare: (DownloadModel) -> Unit,
    onClickPlay: (DownloadModel) -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    onClickDownload: () -> Unit,
    onClickViewAll: () -> Unit
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = Dp48)
        ) {
            item {
                Text(
                    text = "Video Saver",
                    style = typography.h2.copy(fontSize = Sp22),
                    color = colorScheme.onPrimary
                )
            }
            item {
                Spacer(modifier = Modifier.size(Dp16))
                Text(
                    text = "Easiest way to download your favorite video content from the biggest social media platforms like Facebook, Instagram, TikTok, Twitter, and YouTube.",
                    style = typography.body2,
                    color = colorScheme.secondary
                )
            }
            item {
                Spacer(modifier = Modifier.size(Dp24))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = SpaceBetween
                ) {
                    PlatformType.entries.filter { it.platformName != UNKNOWN }.map {
                        MeverPlatformIcon(
                            platform = it.platformName,
                            iconSize = Dp48
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(Dp24))
                MeverTextField(
                    modifier = Modifier.fillMaxWidth(),
                    webDomainValue = urlSocialMediaState,
                    onValueChange = { onValueChange(it) }
                )
            }
            item {
                Spacer(modifier = Modifier.size(Dp10))
                MeverButton(
                    enabled = urlSocialMediaState.text.trim().getPlatformType() != PlatformType.UNKNOWN,
                    isLoading = isLoading
                ) { onClickDownload() }
                Spacer(modifier = Modifier.size(Dp24))
            }
            if (downloadList.isNotEmpty()) stickyHeader {
                Row(
                    modifier = Modifier
                        .background(color = colorScheme.background)
                        .fillMaxWidth()
                        .padding(vertical = Dp16),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = SpaceBetween
                ) {
                    Text(
                        text = "Recently Downloaded",
                        style = typography.bodyBold1,
                        color = colorScheme.onPrimary
                    )
                    Text(
                        text = "View All",
                        style = typography.body2,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .padding(Dp4)
                            .clip(RoundedCornerShape(Dp8))
                            .clickableSingle { onClickViewAll() }
                    )
                }
            }
            items(
                items = downloadList.take(5),
                key = { it.id }
            ) {
                MeverCard(
                    modifier = Modifier
                        .padding(vertical = Dp12)
                        .animateItem(),
                    meverCardArgs = MeverCardArgs(
                        image = it.url,
                        tag = it.tag,
                        metaData = it.metaData,
                        fileName = it.fileName,
                        status = it.status,
                        progress = it.progress,
                        total = it.total,
                        path = it.path,
                        type = DOWNLOADED,
                        iconSize = Dp40,
                        iconPadding = Dp8,
                        onClickDownloading = { onClickDownloading(it) },
                        onClickDelete = { onClickDelete(it) },
                        onClickShare = { onClickShare(it) },
                        onClickPlay = { onClickPlay(it) }
                    )
                )
            }
        }
    }
}

private fun handleClickActionMenu(navigator: BaseNavigator) = { name: String ->
    when (name) {
        NOTIFICATION -> navigator.navigateToNotificationScreen()
        GALLERY -> navigator.navigateToGalleryScreen()
        SETTING -> navigator.run { navigate(getNavGraph<SettingNavGraph>().getSettingLandingRoute()) }
        else -> Unit
    }
}

private fun BaseNavigator.navigateToNotificationScreen() =
    navigate(getNavGraph<NotificationNavGraph>().getNotificationLandingRoute())

private fun BaseNavigator.navigateToGalleryScreen() =
    navigate(getNavGraph<GalleryNavGraph>().getGalleryLandingRoute())