package com.dapascript.mever.feature.home.screen

import android.content.Context
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIcon
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIconBackgroundColor
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverIcon
import com.dapascript.mever.core.common.ui.component.MeverTabs
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp210
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.Constant.PlatformName.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Available
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.getNetworkStatus
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetDownload
import com.dapascript.mever.feature.home.screen.component.HandleDialogError
import com.dapascript.mever.feature.home.screen.component.HandleDialogPermission
import com.dapascript.mever.feature.home.screen.component.HandleDialogYoutubeQuality
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel
import com.ketch.DownloadModel
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.SUCCESS
import kotlinx.coroutines.launch
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN as UNKNOWN_TYPE
import com.dapascript.mever.feature.home.R as FeatureHomeR

@Composable
internal fun HomeLandingScreen(
    navController: NavController,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val contentState = contentState.collectAsStateValue()
    val isNetworkAvailable = connectivityObserver.observe().collectAsState(
        connectivityObserver.isConnected()
    )
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    var contents by remember { mutableStateOf<List<ContentEntity>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }
    var showYoutubeChooseQualityModal by remember { mutableStateOf(false) }
    var showErrorNetworkModal by remember { mutableStateOf(false) }
    var showErrorResponseModal by remember { mutableStateOf<Throwable?>(null) }
    val storagePermLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) {
        val allGranted = getStoragePermission.all { permissions -> it[permissions] == true }
        if (allGranted) getNetworkStatus(
            isNetworkAvailable = isNetworkAvailable.value,
            onNetworkAvailable = {
                if (urlSocialMediaState.text.getPlatformType() == YOUTUBE) {
                    showYoutubeChooseQualityModal = true
                } else getApiDownloader(urlSocialMediaState)
            },
            onNetworkUnavailable = { showErrorNetworkModal = true }
        ) else getStoragePermission.forEach { permission ->
            onPermissionResult(permission, isGranted = it[permission] == true)
        }
    }

    BaseScreen(
        hideDefaultTopBar = true,
        allowScreenOverlap = true
    ) {
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
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            listContent = contents,
            showBottomSheet = contents.isNotEmpty(),
            isFailedFetchImage = isNetworkAvailable.value != Available,
            onClickDownload = { url ->
                downloadFile(
                    url = url,
                    platformName = urlSocialMediaState.text.getPlatformType().platformName,
                    thumbnail = contents.firstOrNull()?.thumbnail.orEmpty()
                )
                contents = emptyList()
                urlSocialMediaState = urlSocialMediaState.copy(text = "")
            },
            onClickDismiss = { contents = emptyList() }
        )

        HandleDialogError(
            showDialog = showErrorNetworkModal,
            errorTitle = stringResource(R.string.no_internet_title),
            errorDescription = stringResource(R.string.no_internet_desc),
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
            errorTitle = stringResource(R.string.unknown_error_title),
            errorDescription = stringResource(R.string.unknown_error_desc),
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
                storagePermLauncher.launch(getStoragePermission)
            },
            onDismiss = ::dismissDialog
        )

        HandleDialogYoutubeQuality(
            showDialog = showYoutubeChooseQualityModal,
            onApplyQuality = { quality ->
                showYoutubeChooseQualityModal = false
                selectedQuality = quality
                getApiDownloader(urlSocialMediaState)
            },
            onDismiss = { showYoutubeChooseQualityModal = false }
        )

        HomeScreenContent(
            viewModel = this,
            navController = navController,
            isLoading = showLoading
        ) { storagePermLauncher.launch(getStoragePermission) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent(
    viewModel: HomeLandingViewModel,
    navController: NavController,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    requestStoragePermissionLauncher: () -> Unit
) = with(viewModel) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        val context = LocalContext.current
        val downloadList = downloadList.collectAsStateValue()
        val getListActionMenu = remember { getListActionMenu(context) }
        val tabItems = remember { tabItems(context) }
        val pagerState = rememberPagerState(pageCount = { tabItems.size })
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
        var showFailedDialog by remember { mutableStateOf<Int?>(null) }

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
                    actionMenus = getListActionMenu.map { (name, resource) ->
                        ActionMenu(
                            icon = resource,
                            nameIcon = name,
                            showBadge = showBadge && name == stringResource(R.string.gallery),
                        ) { handleClickActionMenu(context, navController)(name) }
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
                                    else Offset(
                                        x = 0f,
                                        y = -scrollState.dispatchRawDelta(-available.y)
                                    )
                                }
                            })
                    ) { index ->
                        when (index) {
                            0 -> DownloaderSection(
                                downloadList = downloadList,
                                isLoading = isLoading,
                                urlSocialMediaState = urlSocialMediaState,
                                onClickCard = { model ->
                                    with(model) {
                                        when (status) {
                                            SUCCESS -> navController.navigateTo(
                                                GalleryContentDetailRoute(
                                                    id = id,
                                                    sourceFile = getMeverFiles()?.find { file ->
                                                        file.name == fileName
                                                    }?.path.orEmpty(),
                                                    fileName = fileName.replaceTimeFormat()
                                                )
                                            )

                                            FAILED -> showFailedDialog = id
                                            PAUSED -> ketch.resume(id)
                                            else -> ketch.pause(id)
                                        }
                                    }
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
                                onValueChange = { urlSocialMediaState = it },
                                onClickDownload = {
                                    if (isLoading.not()) requestStoragePermissionLauncher()
                                },
                                onClickViewAll = { navController.navigateToGalleryScreen() }
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
                    title = stringResource(R.string.delete_title),
                    primaryButtonText = stringResource(R.string.delete_button),
                    onClickPrimaryButton = {
                        ketch.clearDb(id)
                        showDeleteDialog = null
                    },
                    onClickSecondaryButton = { showDeleteDialog = null }
                )
            ) {
                Text(
                    text = stringResource(R.string.delete_desc),
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
            }
        }

        showFailedDialog?.let { id ->
            MeverDialog(
                showDialog = true,
                meverDialogArgs = MeverDialogArgs(
                    title = stringResource(R.string.download_failed_title),
                    primaryButtonText = stringResource(R.string.delete_button),
                    secondaryButtonText = stringResource(R.string.retry),
                    onClickPrimaryButton = {
                        ketch.clearDb(id)
                        showFailedDialog = null
                    },
                    onClickSecondaryButton = {
                        ketch.retry(id)
                        showFailedDialog = null
                    }
                )
            ) {
                Text(
                    text = stringResource(R.string.download_failed_desc),
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DownloaderSection(
    isLoading: Boolean,
    urlSocialMediaState: TextFieldValue,
    downloadList: List<DownloadModel>?,
    modifier: Modifier = Modifier,
    onClickCard: (DownloadModel) -> Unit,
    onClickDelete: (DownloadModel) -> Unit,
    onClickShare: (DownloadModel) -> Unit,
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
                    text = stringResource(R.string.downloader_title),
                    style = typography.h2.copy(fontSize = Sp22),
                    color = colorScheme.onPrimary
                )
            }
            item {
                Spacer(modifier = Modifier.size(Dp16))
                Text(
                    text = stringResource(R.string.downloader_desc),
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
                        MeverIcon(
                            icon = getPlatformIcon(it.platformName),
                            iconBackgroundColor = getPlatformIconBackgroundColor(it.platformName),
                            iconSize = Dp48,
                            iconPadding = Dp10
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp40),
                    title = stringResource(R.string.download),
                    buttonType = FILLED,
                    isEnabled = urlSocialMediaState.text.trim().getPlatformType() != UNKNOWN_TYPE,
                    isLoading = isLoading
                ) { onClickDownload() }
                Spacer(modifier = Modifier.size(Dp24))
            }
            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(color = colorScheme.background)
                        .fillMaxWidth()
                        .padding(top = Dp16),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.recently_downloaded),
                        style = typography.bodyBold1,
                        color = colorScheme.onPrimary
                    )
                    if (downloadList.isNullOrEmpty().not()) Text(
                        text = stringResource(R.string.view_all),
                        style = typography.body2,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .animateItem()
                            .clip(RoundedCornerShape(Dp8))
                            .onCustomClick { onClickViewAll() }
                    )
                }
            }
            downloadList?.let { files ->
                if (files.isNotEmpty()) items(
                    items = files.take(5),
                    key = { it.id }
                ) {
                    MeverCard(
                        modifier = Modifier.animateItem(),
                        cardArgs = MeverCardArgs(
                            source = it.url,
                            tag = it.tag,
                            fileName = it.fileName,
                            status = it.status,
                            progress = it.progress,
                            total = it.total,
                            path = it.path,
                            urlThumbnail = it.metaData,
                            icon = getPlatformIcon(it.tag),
                            iconBackgroundColor = getPlatformIconBackgroundColor(it.tag),
                            iconSize = Dp24,
                            iconPadding = Dp5
                        ),
                        onClickCard = { onClickCard(it) },
                        onClickShare = { onClickShare(it) },
                        onClickDelete = { onClickDelete(it) }
                    )
                } else item {
                    MeverEmptyItem(
                        image = R.drawable.ic_gallery_empty,
                        size = Dp210,
                        description = stringResource(R.string.empty_list_desc),
                    )
                }
            }
        }
    }
}

private fun getListActionMenu(context: Context) = listOf(
    context.getString(R.string.gallery) to FeatureHomeR.drawable.ic_explore,
    context.getString(R.string.settings) to FeatureHomeR.drawable.ic_setting
)

private fun tabItems(context: Context) = listOf(
    context.getString(R.string.downloader_tab),
    context.getString(R.string.ai_tab)
)

private fun handleClickActionMenu(context: Context, navController: NavController) =
    { name: String ->
        when (name) {
            context.getString(R.string.gallery) -> navController.navigateToGalleryScreen()
            context.getString(R.string.settings) -> navController.navigateToSettingScreen()
            else -> Unit
        }
    }

private fun NavController.navigateToGalleryScreen() = navigateTo(GalleryLandingRoute)

private fun NavController.navigateToSettingScreen() = navigateTo(SettingLandingRoute)