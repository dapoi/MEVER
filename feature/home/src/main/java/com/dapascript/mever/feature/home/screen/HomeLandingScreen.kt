package com.dapascript.mever.feature.home.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.GalleryNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.getCardType
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverPlatformIcon
import com.dapascript.mever.core.common.ui.component.MeverTabs
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp210
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.Constant.PlatformName.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.getNetworkStatus
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.model.local.ContentEntity
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.listOfActionMenu
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetDownload
import com.dapascript.mever.feature.home.screen.component.HandleDialogError
import com.dapascript.mever.feature.home.screen.component.HandleDialogPermission
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel
import com.ketch.DownloadModel
import com.ketch.Status.PAUSED
import com.ketch.Status.SUCCESS
import kotlinx.coroutines.launch

@Composable
internal fun HomeLandingScreen(
    navigator: BaseNavigator,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val contentState = contentState.collectAsStateValue()
    val isNetworkAvailable = connectivityObserver.observe().collectAsState(connectivityObserver.isConnected())
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    var contents by remember { mutableStateOf<List<ContentEntity>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }
    var showErrorNetworkModal by remember { mutableStateOf(false) }
    var showErrorResponseModal by remember { mutableStateOf<Throwable?>(null) }
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
        hideTopBar = true,
        allowScreenOverlap = true,
        statusBarColor = colorScheme.background,
        navigationBarColor = colorScheme.background
    ) {
        LaunchedEffect(Unit) { getObservableKetch() }

        LaunchedEffect(contentState) {
            contentState.handleUiState(
                onLoading = { showLoading = true },
                onSuccess = { result ->
                    showLoading = false
                    contents = result
                },
                onFailed = {
                    showLoading = false
                    showErrorResponseModal = it
                }
            )
        }

        HandleBottomSheetDownload(
            listContent = contents,
            showBottomSheet = contents.isNotEmpty(),
            onClickDownload = { url ->
                contents = emptyList()
                downloadFile(
                    url = url,
                    platformName = urlSocialMediaState.text.getPlatformType().platformName
                )
                urlSocialMediaState = urlSocialMediaState.copy(text = "")
            },
            onClickDismiss = { contents = emptyList() }
        )

        HandleDialogError(
            showDialog = showErrorNetworkModal,
            errorTitle = "Ups, You're Offline",
            errorDescription = "Please check your internet connection and try again.",
            errorImage = R.drawable.ic_offline,
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

        HandleDialogError(
            showDialog = showErrorResponseModal != null,
            errorTitle = "Something Went Wrong!",
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

        HomeScreenContent(
            viewModel = this,
            navigator = navigator,
            isLoading = showLoading
        ) { requestStoragePermissionLauncher.launch(getStoragePermission) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent(
    viewModel: HomeLandingViewModel,
    navigator: BaseNavigator,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    requestStoragePermissionLauncher: () -> Unit
) = with(viewModel) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val context = LocalContext.current
        val pagerState = rememberPagerState(pageCount = { tabItems.size })
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        var showDeleteDialog by remember { mutableStateOf<Int?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = CenterHorizontally
        ) {
            MeverTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp24),
                topBarArgs = TopBarArgs(
                    screenName = null,
                    actionMenus = listOfActionMenu.map { (name, resource) ->
                        ActionMenu(
                            icon = resource,
                            nameIcon = name,
                            showBadge = showBadge && name == GALLERY,
                        ) { handleClickActionMenu(navigator)(name) }
                    }
                )
            )
            Column(
                modifier = Modifier.height(this@BoxWithConstraints.maxHeight),
                horizontalAlignment = CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(Dp16))
                MeverTabs(
                    items = tabItems,
                    pagerState = pagerState
                ) { scope.launch { pagerState.animateScrollToPage(it) } }
                Spacer(modifier = Modifier.size(Dp24))
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .nestedScroll(remember {
                                object : NestedScrollConnection {
                                    override fun onPreScroll(
                                        available: Offset,
                                        source: NestedScrollSource
                                    ) = if (available.y > 0) Offset.Zero
                                    else Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                                }
                            })
                    ) { index ->
                        when (index) {
                            0 -> HomeVideoSection(
                                downloadList = downloadList,
                                isLoading = isLoading,
                                urlSocialMediaState = urlSocialMediaState,
                                onClickDownloading = {
                                    if (it.status == PAUSED) ketch.resume(it.id) else ketch.pause(it.id)
                                },
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
            contentPadding = PaddingValues(bottom = Dp48, start = Dp24, end = Dp24)
        ) {
            item {
                Text(
                    text = "Media Saver",
                    style = typography.h2.copy(fontSize = Sp22),
                    color = colorScheme.onPrimary
                )
            }
            item {
                Spacer(modifier = Modifier.size(Dp16))
                Text(
                    text = "Easiest way to download your favorite content from the biggest social media platforms like Facebook, Instagram, TikTok, Twitter (X), and YouTube.",
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
            stickyHeader {
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
                    if (downloadList.isNotEmpty()) Text(
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
            if (downloadList.isNotEmpty()) items(
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
                        type = getCardType(it.status),
                        iconSize = if (it.status == SUCCESS) Dp40 else Dp24,
                        iconPadding = if (it.status == SUCCESS) Dp8 else Dp5,
                        onClickDownloading = { onClickDownloading(it) },
                        onClickDelete = { onClickDelete(it) },
                        onClickShare = { onClickShare(it) },
                        onClickPlay = { onClickPlay(it) }
                    )
                )
            } else item {
                MeverEmptyItem(
                    image = R.drawable.ic_gallery_empty,
                    size = Dp210,
                    description = "Looks like there’s nothing here... Download something to get content!"
                )
            }
        }
    }
}

private fun handleClickActionMenu(navigator: BaseNavigator) = { name: String ->
    when (name) {
        GALLERY -> navigator.navigateToGalleryScreen()
        SETTING -> navigator.navigateToSettingScreen()
        else -> Unit
    }
}

private fun BaseNavigator.navigateToGalleryScreen() = navigate(getNavGraph<GalleryNavGraph>().getGalleryLandingRoute())

private fun BaseNavigator.navigateToSettingScreen() = navigate(getNavGraph<SettingNavGraph>().getSettingLandingRoute())