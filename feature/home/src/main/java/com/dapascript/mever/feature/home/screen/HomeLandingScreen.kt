package com.dapascript.mever.feature.home.screen

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIcon
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDeclinedPermissionDialog
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverFeatureCard
import com.dapascript.mever.core.common.ui.component.MeverIcon
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.component.showShadow
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp160
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.DpHalf
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.InAppUpdateManager
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.X
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.clearFocusOnKeyboardDismiss
import com.dapascript.mever.core.common.util.formatHighlightedText
import com.dapascript.mever.core.common.util.getExtensionFromUrl
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.handleClickButton
import com.dapascript.mever.core.common.util.isMusic
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.navigateToMusic
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.storage.StorageUtil.StorageInfo
import com.dapascript.mever.core.common.util.storage.StorageUtil.getStorageInfo
import com.dapascript.mever.core.common.util.storage.StorageUtil.isStorageFull
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.AI_IMAGE
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.FIND_IMAGE
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.REMOVE_BG
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeQuickToolsRoute.FeatureCard.QuickToolsType.WA
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getCardColor
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getRoute
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetDownload
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetPlatformSupport
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetYouTubeQuality
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.ketch.DownloadModel
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.SUCCESS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.System.currentTimeMillis
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import androidx.compose.foundation.layout.Arrangement.Center as ArrangementCenter
import com.dapascript.mever.feature.home.R as FeatureHomeR

@Composable
internal fun HomeLandingScreen(
    navigator: Navigator,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        hideDefaultTopBar = true,
        useStatusBarsPadding = true,
        onBackHandler = { navigator.goBack() }
    ) {
        var isUpdateReady by remember { mutableStateOf(false) }
        var isUpdateRefused by rememberSaveable { mutableStateOf(false) }
        val activity = LocalActivity.current
        val context = LocalContext.current
        val showBadge = showBadge.collectAsStateValue()
        val getButtonClickCount = getButtonClickCount.collectAsStateValue()
        val scope = rememberCoroutineScope()
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        val inAppUpdateManager = remember { InAppUpdateManager(activity) }
        val updateLauncher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { }

        LaunchedEffect(Unit) {
            inAppUpdateManager.registerListener { isUpdateReady = true }
            inAppUpdateManager.startUpdate(
                updateType = FLEXIBLE,
                updateAvailability = UPDATE_AVAILABLE,
                launcher = updateLauncher,
                onUpdateNotAvailable = {}
            )
            withContext(IO) { storageInfo = getStorageInfo(context) }
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == ON_RESUME && isUpdateRefused.not()) {
                    inAppUpdateManager.checkForDownloadedUpdate { isUpdateReady = true }
                }
            }
            lifecycleOwner.value.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.value.lifecycle.removeObserver(observer)
                inAppUpdateManager.unregisterListener()
            }
        }

        MeverDialog(
            showDialog = isUpdateReady && isUpdateRefused.not(),
            image = null,
            title = stringResource(R.string.update_available),
            description = stringResource(R.string.update_has_completed),
            primaryActionLabel = stringResource(R.string.ok),
            secondaryActionLabel = stringResource(R.string.later),
            onClickPrimaryAction = {
                inAppUpdateManager.completeUpdate()
                isUpdateReady = false
            },
            onClickSecondaryAction = {
                isUpdateReady = false
                isUpdateRefused = true
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            HomeLandingContent(
                modifier = Modifier.matchParentSize(),
                viewModel = this@with,
                context = context,
                activity = activity,
                scope = scope,
                navigator = navigator,
                getButtonClickCount = getButtonClickCount,
                showBadge = showBadge,
                lifecycleOwner = lifecycleOwner
            )
            if (showBadge) MeverBannerAd(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(BottomCenter)
            )
        }
    }
}

@Composable
private fun HomeLandingContent(
    viewModel: HomeLandingViewModel,
    context: Context,
    activity: ComponentActivity,
    scope: CoroutineScope,
    navigator: Navigator,
    getButtonClickCount: Int,
    showBadge: Boolean,
    lifecycleOwner: State<LifecycleOwner>,
    modifier: Modifier = Modifier
) = with(viewModel) {
    val deviceType = LocalDeviceType.current
    val downloadList = downloadList.collectAsStateValue()
    val downloaderResponseState = downloaderResponseState.collectAsStateValue()
    val youtubeResolutions = youtubeResolutions.collectAsStateValue()
    val urlIntent = getUrlIntent.collectAsStateValue()
    var showLoading by remember { mutableStateOf(false) }
    var showYoutubeChooseQualityModal by remember { mutableStateOf(false) }
    var randomDonateDialogOffer by remember { mutableIntStateOf(0) }
    var checkStoragePermissions by remember { mutableStateOf<List<String>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var showFailedDialog by remember { mutableStateOf<Int?>(null) }
    var showUnsupportedYouTubeDialog by remember { mutableStateOf(false) }
    var showPlatformSupportDialog by remember { mutableStateOf(false) }
    var isStorageFull by remember { mutableStateOf(false) }
    var isPlaylistNotSupported by remember { mutableStateOf(false) }
    var loadingItemIndex by remember { mutableStateOf<Int?>(null) }
    var isDownloadProcessing by remember { mutableStateOf(false) }
    var isInPreview by remember { mutableStateOf(false) }
    val onClickCardAction: (DownloadModel) -> Unit = { download ->
        with(download) {
            when (status) {
                SUCCESS -> {
                    if (isMusic(fileName).not()) {
                        navigator.navigate(
                            GalleryContentDetailRoute(
                                contents = downloadList.orEmpty().filterNot {
                                    isMusic(it.fileName)
                                }.map {
                                    Content(
                                        id = it.id,
                                        isVideo = isVideo(it.path),
                                        media = it.path,
                                        fileName = it.fileName
                                    )
                                },
                                initialIndex = downloadList.orEmpty().filterNot {
                                    isMusic(it.fileName)
                                }.indexOfFirst { it.id == id },
                            )
                        )
                    } else {
                        navigateToMusic(
                            context = context,
                            file = File(path)
                        )
                    }
                }

                FAILED -> showFailedDialog = id
                PAUSED -> resumeDownload(id)
                else -> pauseDownload(id)
            }
        }
    }
    val interstitialController = rememberInterstitialAd {
        checkStoragePermissions = getStoragePermission()
    }

    LaunchedEffect(downloadList) {
        downloadList
            ?.filter { it.status == SUCCESS }
            ?.forEach {
                syncToGallery(
                    context,
                    it.fileName
                )
            }
    }

    LaunchedEffect(urlIntent) {
        if (urlIntent.isNotEmpty()) {
            urlSocialMediaState = TextFieldValue(urlIntent)
            resetUrlIntent()
            checkStoragePermissions = getStoragePermission()
        }
    }

    LaunchedEffect(lifecycleOwner.value) {
        lifecycleOwner.value.lifecycle.repeatOnLifecycle(RESUMED) { refreshDatabase() }
    }

    LaunchedEffect(downloaderResponseState) {
        downloaderResponseState.handleUiState(
            onLoading = { showLoading = true },
            onSuccess = { showLoading = false },
            onFailed = { showLoading = false }
        )
    }

    LaunchedEffect(randomDonateDialogOffer, shouldShowDonationOfferDialog) {
        delay(1.seconds)
        if (shouldShowDonationOfferDialog) {
            (0..2).random(Random).also { randomValue ->
                randomDonateDialogOffer = randomValue
                shouldShowDonationOfferDialog = false
            }
        }
    }

    if (checkStoragePermissions.isNotEmpty()) {
        MeverPermissionHandler(
            permissions = checkStoragePermissions,
            onGranted = {
                checkStoragePermissions = emptyList()
                checkStateBeforeDownload(
                    urlSocialMediaState = urlSocialMediaState,
                    storageInfo = storageInfo,
                    onActionStorageFull = {
                        isStorageFull = true
                        errorMessage = context.getString(R.string.storage_full)
                    },
                    onActionIsContentPlaylist = {
                        isPlaylistNotSupported = true
                        errorMessage = context.getString(R.string.playlist_not_supported)
                    },
                    onActionIsContentYT = {
                        if (youtubeResolutions.isNotEmpty()) showYoutubeChooseQualityModal = true
                        else showUnsupportedYouTubeDialog = true
                    },
                    onActionDownload = { getApiDownloader() }
                )
            },
            onDenied = { isPermanentlyDeclined, retry ->
                MeverDeclinedPermissionDialog(
                    isPermissionsDeclined = isPermanentlyDeclined,
                    onGoToSetting = {
                        checkStoragePermissions = emptyList()
                        scope.launch { activity.goToSetting() }
                    },
                    onRetry = { retry() },
                    onDismiss = { checkStoragePermissions = emptyList() }
                )
            }
        )
    }

    HandleBottomSheetDownload(
        modifier = Modifier.fillMaxWidth(),
        listContent = contents,
        isDownloadProcessing = isDownloadProcessing,
        isInPreview = isInPreview,
        loadingItemIndex = loadingItemIndex,
        onClickDownload = { urls ->
            isDownloadProcessing = true
            scope.launch {
                val byUrl = contents.associateBy { it.url }
                val semaphore = Semaphore(3)

                try {
                    supervisorScope {
                        urls.mapIndexed { index, url ->
                            async(IO) {
                                semaphore.withPermit {
                                    runCatching {
                                        val content = byUrl[url] ?: return@runCatching
                                        val extension = getExtensionFromUrl(
                                            url = url,
                                            extensionFromResponse = content.type
                                        ) ?: run {
                                            val type = content.type.lowercase()
                                            when {
                                                type.contains("video") || url.contains("mp4") -> ".mp4"
                                                type.contains("image") || url.contains("jpg") || url.contains(
                                                    "jpeg"
                                                ) -> ".jpg"

                                                type.contains("audio") || url.contains("mp3") -> ".mp3"
                                                else -> ".jpg"
                                            }
                                        }
                                        val timeStamp = changeToCurrentDate(currentTimeMillis())
                                        val fileName = content.fileName.ifEmpty {
                                            "MEVER_${timeStamp}_${index}${extension}"
                                        }

                                        startDownload(
                                            url = url,
                                            fileName = fileName,
                                            thumbnail = content.thumbnail
                                        )
                                    }.onFailure { it.printStackTrace() }
                                }
                            }
                        }.awaitAll()
                    }
                } finally {
                    isDownloadProcessing = false
                    contents = emptyList()
                }
            }
        },
        onClickPreview = { index ->
            loadingItemIndex = index
            scope.launch {
                try {
                    val processedContents = withContext(Default) {
                        contents[index].let { content ->
                            val extension = getExtensionFromUrl(
                                url = content.url,
                                extensionFromResponse = content.type
                            ).orEmpty()

                            Content(
                                id = index,
                                isPreview = true,
                                isVideo = isVideo(extension),
                                media = content.url,
                                fileName = content.fileName
                            )
                        }
                    }

                    if (loadingItemIndex == index) {
                        isInPreview = true
                        delay(150.milliseconds)
                        navigator.navigate(
                            GalleryContentDetailRoute(
                                contents = listOf(processedContents),
                                initialIndex = index
                            )
                        )
                    }
                } finally {
                    if (loadingItemIndex == index) loadingItemIndex = null
                }
            }
            isInPreview = false
        },
        onClickDismiss = {
            loadingItemIndex = null
            isInPreview = false
            contents = emptyList()
        }
    )

    HandleBottomSheetYouTubeQuality(
        showBottomSheet = showYoutubeChooseQualityModal,
        qualityList = youtubeResolutions.takeIf {
            urlSocialMediaState.text.contains("music").not()
        } ?: listOf(youtubeResolutions.lastOrNull().orEmpty()),
        onApplyQuality = { quality ->
            showYoutubeChooseQualityModal = false
            selectedQuality = quality
            getApiDownloader()
        },
        onDismiss = { showYoutubeChooseQualityModal = false }
    )

    HandleBottomSheetPlatformSupport(
        modifier = Modifier.wrapContentSize(),
        showPlatformSupportDialog = showPlatformSupportDialog,
        youtubeResolutions = youtubeResolutions,
        onDismiss = { showPlatformSupportDialog = false }
    )

    MeverDialog(
        showDialog = showUnsupportedYouTubeDialog,
        description = stringResource(R.string.unsupported_yt),
        primaryActionLabel = stringResource(R.string.ok),
        secondaryActionLabel = null,
        onClickPrimaryAction = { showUnsupportedYouTubeDialog = false }
    )

    MeverDialog(
        showDialog = randomDonateDialogOffer == 1,
        image = R.drawable.ic_coffee,
        title = stringResource(R.string.thanks),
        description = stringResource(R.string.donation_desc),
        primaryActionLabel = stringResource(R.string.sure),
        secondaryActionLabel = stringResource(R.string.later),
        onClickPrimaryAction = {
            randomDonateDialogOffer = 0
            navigator.navigateToSettingScreen(showQrisDialog = true)
        },
        onClickSecondaryAction = { randomDonateDialogOffer = 0 }
    )

    MeverDialog(
        showDialog = errorMessage.isNotEmpty(),
        title = stringResource(R.string.error_title),
        description = errorMessage,
        secondaryActionLabel = if (isPlaylistNotSupported) null else stringResource(R.string.cancel),
        primaryActionLabel = stringResource(
            if (isStorageFull || isPlaylistNotSupported) R.string.ok else R.string.retry
        ),
        onClickPrimaryAction = {
            when {
                isStorageFull -> isStorageFull = false
                isPlaylistNotSupported -> isPlaylistNotSupported = false
                else -> getApiDownloader()
            }
            errorMessage = ""
        },
        onClickSecondaryAction = {
            isStorageFull = false
            errorMessage = ""
        }
    )

    showDeleteDialog?.let { id ->
        MeverDialog(
            showDialog = true,
            image = null,
            title = stringResource(R.string.delete_title),
            description = stringResource(R.string.delete_desc),
            primaryActionLabel = stringResource(R.string.delete_button),
            onClickPrimaryAction = {
                delete(id)
                showDeleteDialog = null
            },
            onClickSecondaryAction = { showDeleteDialog = null }
        )
    }

    showFailedDialog?.let { id ->
        MeverDialog(
            showDialog = true,
            title = stringResource(R.string.download_failed_title),
            description = stringResource(R.string.download_failed_desc),
            primaryActionLabel = stringResource(R.string.delete_button),
            secondaryActionLabel = stringResource(R.string.retry),
            onClickPrimaryAction = {
                delete(id)
                showFailedDialog = null
            },
            onClickSecondaryAction = {
                retryDownload(id)
                showFailedDialog = null
            }
        )
    }

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = if (showBadge) Dp64 else Dp0)
        ) {
            item {
                MeverTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dp24),
                    topBarArgs = TopBarArgs(
                        iconBack = R.drawable.ic_mever,
                        actionMenus = getListActionMenu(
                            context = context,
                            hasDownloadProgress = showBadge
                        ).map { (name, resource) ->
                            ActionMenu(
                                icon = resource,
                                nameIcon = name,
                                showBadge = showBadge && name == stringResource(R.string.gallery),
                            ) {
                                navigator.handleClickActionMenu(
                                    context = context,
                                    name = name
                                )
                            }
                        }
                    )
                )
            }
            if (deviceType == PHONE) {
                item {
                    HeaderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dp24, end = Dp24, top = Dp6)
                    )
                }
                item {
                    DownloaderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dp24, end = Dp24, top = Dp16),
                        isLoading = showLoading,
                        getButtonClickCount = getButtonClickCount,
                        onIncrementClickCount = { incrementClickCount() },
                        onShowAds = { interstitialController.showAd() },
                        onClickSeeSupportedPlatform = { showPlatformSupportDialog = true },
                        onClickDownload = { url ->
                            checkStoragePermissions = getStoragePermission()
                            urlSocialMediaState = TextFieldValue(url)
                        }
                    )
                }
                item {
                    QuickToolsSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dp24, end = Dp24, top = Dp32),
                        context = context
                    ) { route -> navigator.navigate(route) }
                }
                item {
                    RecentlyDownloadedSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dp32, horizontal = Dp24)
                            .navigationBarsPadding(),
                        downloadList = downloadList.orEmpty().take(3),
                        onClickViewAll = { navigator.navigateToGalleryScreen() },
                        onClickDelete = { id -> showDeleteDialog = id },
                        onClickShare = { path ->
                            shareContent(
                                context = context,
                                contentPath = path
                            )
                        },
                        onClickCard = onClickCardAction
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24, vertical = Dp32)
                            .navigationBarsPadding(),
                        verticalArrangement = spacedBy(Dp32)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = spacedBy(Dp32)
                        ) {
                            HeaderSection(modifier = Modifier.weight(1f))
                            DownloaderSection(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                isLoading = showLoading,
                                getButtonClickCount = getButtonClickCount,
                                onIncrementClickCount = { incrementClickCount() },
                                onShowAds = { interstitialController.showAd() },
                                onClickSeeSupportedPlatform = { showPlatformSupportDialog = true },
                                onClickDownload = { url ->
                                    checkStoragePermissions = getStoragePermission()
                                    urlSocialMediaState = TextFieldValue(url)
                                }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = spacedBy(Dp32)
                        ) {
                            QuickToolsSection(
                                modifier = Modifier.weight(1f),
                                context = context
                            ) { route ->
                                navigator.navigate(route)
                            }
                            RecentlyDownloadedSection(
                                modifier = Modifier.weight(1f),
                                downloadList = downloadList.orEmpty().take(2),
                                isPhoneDevice = false,
                                onClickViewAll = { navigator.navigateToGalleryScreen() },
                                onClickDelete = { id -> showDeleteDialog = id },
                                onClickShare = { path ->
                                    shareContent(context = context, contentPath = path)
                                },
                                onClickCard = onClickCardAction
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = spacedBy(Dp6)
        ) {
            Text(
                text = formatHighlightedText(
                    fullText = stringResource(R.string.downloader_title),
                    highlightedText = stringResource(R.string.target_title),
                    highlightedColor = colors.alwaysPurple
                ),
                color = colors.blackWhite,
                style = typography.h2
            )
            Text(
                text = stringResource(R.string.downloader_desc),
                style = typography.body3,
                color = colors.alwaysGray
            )
        }
        MeverImage(
            modifier = Modifier.weight(1f),
            source = R.drawable.ic_header
        )
    }
}

@Composable
private fun DownloaderSection(
    isLoading: Boolean,
    getButtonClickCount: Int,
    modifier: Modifier = Modifier,
    onIncrementClickCount: () -> Unit,
    onShowAds: () -> Unit,
    onClickSeeSupportedPlatform: () -> Unit,
    onClickDownload: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var url by rememberSaveable { mutableStateOf("") }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dp24),
        colors = CardDefaults.cardColors(containerColor = colors.whiteDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = Dp2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = Dp16, horizontal = Dp12),
            verticalArrangement = ArrangementCenter
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = spacedBy(Dp12)
            ) {
                Box(
                    modifier = Modifier
                        .size(Dp40)
                        .clip(CircleShape)
                        .background(colors.lightSoftPurpleDark),
                    contentAlignment = Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_link),
                        tint = MeverPurple,
                        contentDescription = null
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        url.ifEmpty {
                            Text(
                                text = stringResource(R.string.paste_url),
                                style = typography.body2,
                                color = colors.alwaysGray
                            )
                        }
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clearFocusOnKeyboardDismiss(focusManager),
                            value = url,
                            singleLine = true,
                            cursorBrush = SolidColor(colors.alwaysGray),
                            textStyle = typography.body2.copy(color = colors.blackWhite),
                            onValueChange = { url = it }
                        )
                    }
                    Spacer(modifier = Modifier.size(Dp4))
                    if (url.isNotEmpty()) {
                        IconButton(
                            modifier = Modifier.size(Dp24),
                            onClick = { url = "" }
                        ) {
                            Icon(
                                modifier = Modifier.size(Dp20),
                                painter = painterResource(R.drawable.ic_clear),
                                tint = colors.alwaysGray,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
                MeverButton(
                    modifier = Modifier.size(Dp40),
                    title = "",
                    buttonType = Filled(
                        backgroundColor = colors.alwaysPurple,
                        contentColor = MeverWhite
                    ),
                    shape = CircleShape,
                    isEnabled = getPlatformType(url.trim()) != ALL && isLoading.not(),
                    isLoading = isLoading
                ) {
                    handleClickButton(
                        buttonClickCount = getButtonClickCount,
                        onIncrementClickCount = { onIncrementClickCount() },
                        onShowAds = { onShowAds() },
                        onClickAction = { if (isLoading.not()) onClickDownload(url.trim()) }
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dp16),
                thickness = DpHalf,
                color = colors.lightGrayGray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.platforms_supported),
                    style = typography.label3,
                    color = colors.alwaysGray
                )
                Row(
                    modifier = Modifier.onCustomClick { onClickSeeSupportedPlatform() },
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(Dp16)
                ) {
                    Row(horizontalArrangement = spacedBy(Dp6)) {
                        val displayedPlatforms = listOf(
                            FACEBOOK,
                            INSTAGRAM,
                            TIKTOK,
                            X,
                            PINTEREST
                        )
                        val otherCount = PlatformType.entries.count {
                            it !in displayedPlatforms && it !in listOf(
                                PlatformType.AI,
                                ALL,
                                PlatformType.EXPLORE,
                                YOUTUBE,
                                PlatformType.YOUTUBE_MUSIC,
                                PlatformType.DOUYIN
                            )
                        }

                        displayedPlatforms.forEach { type ->
                            MeverIcon(
                                icon = getPlatformIcon(type.platformName),
                                iconShadowColor = colors.purpleTransparent,
                                iconBackgroundColor = colors.whiteDark,
                                iconSize = Dp24,
                                iconPadding = Dp5
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(Dp24)
                                .showShadow(colors.purpleTransparent)
                                .background(
                                    color = colors.lightSoftPurpleDark,
                                    shape = CircleShape
                                ),
                            contentAlignment = Center
                        ) {
                            Text(
                                text = "+$otherCount",
                                textAlign = TextAlign.Center,
                                style = typography.bodyBold3,
                                color = colors.alwaysPurple
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickToolsSection(
    context: Context,
    modifier: Modifier = Modifier,
    onClick: (NavKey) -> Unit
) {
    val deviceType = LocalDeviceType.current

    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp16)
    ) {
        val featuresCard = remember(context) { getFeatureCards(context) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.quick_tools),
                style = typography.h3,
                color = colors.blackWhite
            )
            Text(
                modifier = Modifier.onCustomClick {
                    onClick(HomeQuickToolsRoute(featureCards = featuresCard))
                },
                text = stringResource(R.string.all_tools),
                style = typography.bodyBold2,
                color = colors.alwaysPurple
            )
        }

        if (deviceType == PHONE) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = spacedBy(Dp16)
            ) {
                featuresCard.take(2).forEach { data ->
                    MeverFeatureCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        icon = data.icon,
                        title = data.featureName,
                        desc = data.featureDesc,
                        cardColor = data.toolsType.getCardColor(),
                        titleStyle = typography.bodyBold2,
                        descStyle = typography.body3
                    ) { onClick(data.toolsType.getRoute()) }
                }
            }
        } else {
            featuresCard.take(2).forEach { data ->
                MeverFeatureCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = data.icon,
                    title = data.featureName,
                    desc = data.featureDesc,
                    cardColor = data.toolsType.getCardColor(),
                    titleStyle = typography.bodyBold2,
                    descStyle = typography.body3
                ) { onClick(data.toolsType.getRoute()) }
            }
        }
    }
}

@Composable
private fun RecentlyDownloadedSection(
    downloadList: List<DownloadModel>,
    modifier: Modifier = Modifier,
    isPhoneDevice: Boolean = true,
    onClickViewAll: () -> Unit,
    onClickDelete: (Int) -> Unit,
    onClickShare: (String) -> Unit,
    onClickCard: (DownloadModel) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recently_downloaded),
                style = typography.h3,
                color = colors.blackWhite
            )
            if (downloadList.isNotEmpty()) Text(
                modifier = Modifier.onCustomClick { onClickViewAll() },
                text = stringResource(R.string.view_all),
                style = typography.bodyBold2,
                color = colors.alwaysPurple
            )
        }
        if (downloadList.isEmpty()) {
            MeverEmptyItem(
                modifier = Modifier.fillMaxWidth(),
                image = R.drawable.ic_empty_state,
                imageSize = Dp160,
                title = stringResource(R.string.no_downloads),
                description = stringResource(R.string.empty_list_desc),
                isHorizontal = isPhoneDevice
            )
        } else {
            downloadList.forEach { download ->
                MeverCard(
                    modifier = Modifier
                        .padding(top = Dp16)
                        .clip(RoundedCornerShape(Dp12)),
                    paddingValues = PaddingValues(vertical = Dp0),
                    cardArgs = MeverCardArgs(
                        source = download.url,
                        tag = download.tag,
                        fileName = download.fileName,
                        status = download.status,
                        progress = download.progress,
                        total = download.total,
                        path = download.path,
                        urlThumbnail = download.metaData,
                        icon = getPlatformIcon(download.tag),
                        iconShadowColor = colors.purpleTransparent,
                        iconBackgroundColor = colors.whiteDark,
                        iconSize = Dp24,
                        iconPadding = Dp5
                    ),
                    onClickDelete = { onClickDelete(download.id) },
                    onClickShare = { onClickShare(download.path) },
                    onClickCard = { onClickCard(download) }
                )
            }
        }
    }
}

private fun getFeatureCards(context: Context) = with(context) {
    setOf(
        FeatureCard(
            featureName = getString(R.string.wa_status),
            featureDesc = getString(R.string.wa_status_desc),
            icon = R.drawable.ic_wa,
            toolsType = WA
        ),
        FeatureCard(
            featureName = getString(R.string.remove_bg),
            featureDesc = getString(R.string.remove_bg_desc),
            icon = R.drawable.ic_remove_bg,
            toolsType = REMOVE_BG
        ),
        FeatureCard(
            featureName = getString(R.string.images_finder),
            featureDesc = getString(R.string.images_finder_desc),
            icon = R.drawable.ic_find_image,
            toolsType = FIND_IMAGE
        ),
        FeatureCard(
            featureName = getString(R.string.ai_image_generator),
            featureDesc = getString(R.string.ai_image_generator_desc),
            icon = R.drawable.ic_awesome,
            toolsType = AI_IMAGE
        )
    )
}

private fun checkStateBeforeDownload(
    urlSocialMediaState: TextFieldValue,
    storageInfo: StorageInfo?,
    onActionStorageFull: () -> Unit,
    onActionIsContentPlaylist: () -> Unit,
    onActionIsContentYT: () -> Unit,
    onActionDownload: () -> Unit
) = when {
    isStorageFull(storageInfo) -> onActionStorageFull()
    urlSocialMediaState.text.contains("playlist") -> onActionIsContentPlaylist()
    getPlatformType(urlSocialMediaState.text) == YOUTUBE -> onActionIsContentYT()
    else -> onActionDownload()
}

private fun getListActionMenu(context: Context, hasDownloadProgress: Boolean) = listOf(
    context.getString(R.string.gallery) to if (hasDownloadProgress) FeatureHomeR.drawable.ic_notification
    else FeatureHomeR.drawable.ic_gallery,
    context.getString(R.string.settings) to FeatureHomeR.drawable.ic_setting
)

private fun Navigator.handleClickActionMenu(context: Context, name: String) = when (name) {
    context.getString(R.string.gallery) -> navigateToGalleryScreen()
    context.getString(R.string.settings) -> navigateToSettingScreen(showQrisDialog = false)
    else -> Unit
}

private fun Navigator.navigateToGalleryScreen() = navigate(GalleryLandingRoute)

private fun Navigator.navigateToSettingScreen(
    showQrisDialog: Boolean
) = navigate(SettingLandingRoute(showQrisDialog))