package com.dapascript.mever.feature.home.screen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIcon
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIconBackgroundColor
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverAutoSizableTextField
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDeclinedPermission
import com.dapascript.mever.core.common.ui.component.MeverDialogError
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverIcon
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.MeverTabs
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp75
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverLightViolet
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.MeverYellow
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp26
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.DESKTOP
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.DeviceType.TABLET
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.changeToCurrentDate
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
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.ExploreScreenRoute.ExploreLandingRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAppreciateRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getArtStyles
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getInspirePrompt
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetDownload
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetPlatformSupport
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetYouTubeQuality
import com.dapascript.mever.feature.home.screen.component.HandleDialogExitConfirmation
import com.dapascript.mever.feature.home.screen.component.HandleDialogUnsupportedYt
import com.dapascript.mever.feature.home.screen.component.HandleDonationDialogOffer
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel
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
import com.dapascript.mever.feature.home.R as FeatureHomeR

@Composable
internal fun HomeLandingScreen(
    navController: NavController,
    deviceType: DeviceType,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        useSystemBarsPadding = true,
        allowScreenOverlap = true,
        hideDefaultTopBar = true
    ) {
        HomeScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            viewModel = this,
            deviceType = deviceType,
            navController = navController
        )
    }
}

@Composable
private fun HomeScreenContent(
    viewModel: HomeLandingViewModel,
    navController: NavController,
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) = with(viewModel) {
    BoxWithConstraints(modifier = modifier) {
        var generateButtonHeight by remember { mutableIntStateOf(0) }
        val context = LocalContext.current
        val showBadge = showBadge.collectAsStateValue()
        val isImageGeneratorFeatureActive = isImageGeneratorFeatureActive.collectAsStateValue()
        val isGoImgFeatureActive = isGoImgFeatureActive.collectAsStateValue()
        val getButtonClickCount = getButtonClickCount.collectAsStateValue()
        val tabItems = remember { tabItems(context) }
        val pagerState = rememberPagerState(pageCount = { tabItems.size })
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        val interstitialController = rememberInterstitialAd {
            navController.navigateToImageGenerator(
                prompt = promptState.text,
                artStyle = selectedArtStyle.second,
                totalImages = selectedImageCount
            )
        }

        Column(
            modifier = Modifier
                .matchParentSize()
                .verticalScroll(scrollState),
            horizontalAlignment = CenterHorizontally
        ) {
            MeverTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp24),
                topBarArgs = TopBarArgs(
                    iconBack = R.drawable.ic_mever,
                    actionMenus = getListActionMenu(context, showBadge)
                        .filterNot {
                            it.first == stringResource(R.string.explore) &&
                                    (isGoImgFeatureActive.not() || deviceType == PHONE &&
                                            isImageGeneratorFeatureActive.not())
                        }
                        .map { (name, resource) ->
                            ActionMenu(
                                icon = resource,
                                nameIcon = name,
                                showBadge = showBadge && name == stringResource(R.string.gallery),
                            ) { navController.handleClickActionMenu(context, name) }
                        }
                )
            )
            Column(
                modifier = Modifier.height(this@BoxWithConstraints.maxHeight),
                horizontalAlignment = CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(Dp16))
                if (deviceType == PHONE) {
                    if (isImageGeneratorFeatureActive) {
                        MeverTabs(
                            items = tabItems,
                            pagerState = pagerState
                        ) { scope.launch { pagerState.animateScrollToPage(it) } }
                        Spacer(modifier = Modifier.size(Dp24))
                    }
                    CompositionLocalProvider(LocalOverscrollFactory provides null) {
                        HorizontalPager(
                            state = pagerState,
                            userScrollEnabled = isImageGeneratorFeatureActive,
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
                                0 -> {
                                    HomeDownloaderSection(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = Dp24)
                                            .navigationBarsPadding(),
                                        viewModel = this@with,
                                        context = context,
                                        navController = navController,
                                        scope = scope,
                                        getButtonClickCount = getButtonClickCount,
                                        isExploreImageFeatureActive = isGoImgFeatureActive,
                                        isImageGeneratorFeatureActive = isImageGeneratorFeatureActive
                                    )
                                }

                                1 -> {
                                    HomeAiSection(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                start = Dp24,
                                                end = Dp24,
                                                bottom = generateButtonHeight.dp
                                            )
                                            .navigationBarsPadding(),
                                        context = context,
                                        deviceType = deviceType,
                                        prompt = promptState.text,
                                        totalImageSelected = selectedImageCount,
                                        artStyleSelected = selectedArtStyle.first,
                                        onPromptChange = {
                                            if (it.length <= 1000 || it.length < promptState.text.length) {
                                                promptState = promptState.copy(text = it)
                                            }
                                        },
                                        onImageCountSelected = { selectedImageCount = it },
                                        onArtStyleSelected = { name, prompt ->
                                            selectedArtStyle = Pair(name, prompt)
                                        }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                    ) {
                        HomeDownloaderSection(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = Dp24),
                            viewModel = this@with,
                            context = context,
                            navController = navController,
                            scope = scope,
                            getButtonClickCount = getButtonClickCount,
                            isExploreImageFeatureActive = isGoImgFeatureActive,
                            isImageGeneratorFeatureActive = isImageGeneratorFeatureActive,
                            isPhoneDevice = false
                        )
                        HomeAiSection(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = Dp24),
                            context = context,
                            deviceType = deviceType,
                            prompt = promptState.text,
                            totalImageSelected = selectedImageCount,
                            artStyleSelected = selectedArtStyle.first,
                            onPromptChange = {
                                if (it.length <= 1000 || it.length < promptState.text.length) {
                                    promptState = promptState.copy(text = it)
                                }
                            },
                            onImageCountSelected = { selectedImageCount = it },
                            onArtStyleSelected = { name, prompt ->
                                selectedArtStyle = Pair(name, prompt)
                            },
                            onClickGenerate = {
                                handleClickButton(
                                    buttonClickCount = getButtonClickCount,
                                    onIncrementClickCount = { incrementClickCount() },
                                    onShowAds = { interstitialController.showAd() },
                                    onClickAction = {
                                        navController.navigateToImageGenerator(
                                            prompt = promptState.text,
                                            artStyle = selectedArtStyle.second,
                                            totalImages = selectedImageCount
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
        if (pagerState.currentPage == 0 && deviceType == PHONE && showBadge) {
            MeverBannerAd(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(BottomCenter)
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(BottomCenter),
            visible = pagerState.currentPage == 1 && deviceType == PHONE,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = Dp24, end = Dp24, bottom = Dp24)
                    .onGloballyPositioned {
                        generateButtonHeight = it.size.height
                    },
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dp16)
            ) {
                MeverButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp40),
                    title = stringResource(R.string.generate),
                    isEnabled = promptState.text.isNotEmpty(),
                    buttonType = Filled(
                        backgroundColor = colorScheme.primary,
                        contentColor = MeverWhite
                    )
                ) {
                    handleClickButton(
                        buttonClickCount = getButtonClickCount,
                        onIncrementClickCount = { incrementClickCount() },
                        onShowAds = { interstitialController.showAd() },
                        onClickAction = {
                            navController.navigateToImageGenerator(
                                prompt = promptState.text,
                                artStyle = selectedArtStyle.second,
                                totalImages = selectedImageCount
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeDownloaderSection(
    viewModel: HomeLandingViewModel,
    context: Context,
    navController: NavController,
    scope: CoroutineScope,
    getButtonClickCount: Int,
    isExploreImageFeatureActive: Boolean,
    isImageGeneratorFeatureActive: Boolean,
    modifier: Modifier = Modifier,
    isPhoneDevice: Boolean = true
) = with(viewModel) {
    val downloadList = downloadList.collectAsStateValue()?.reversed()?.filterNot {
        it.tag == AI.platformName
    }
    val downloaderResponseState = downloaderResponseState.collectAsStateValue()
    val youtubeResolutions = youtubeResolutions.collectAsStateValue()
    val urlIntent = getUrlIntent.collectAsStateValue()
    val activity = LocalActivity.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    var showLoading by remember { mutableStateOf(false) }
    var showCancelExitConfirmation by remember { mutableStateOf(false) }
    var showYoutubeChooseQualityModal by remember { mutableStateOf(false) }
    var randomDonateDialogOffer by remember { mutableIntStateOf(0) }
    var setStoragePermission by remember { mutableStateOf<List<String>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var showFailedDialog by remember { mutableStateOf<Int?>(null) }
    var showUnsupportedYouTubeDialog by remember { mutableStateOf(false) }
    var showPlatformSupportDialog by remember { mutableStateOf(false) }
    var isStorageFull by remember { mutableStateOf(false) }
    var loadingItemIndex by remember { mutableStateOf<Int?>(null) }
    var isDownloadProcessing by remember { mutableStateOf(false) }
    var isInPreview by remember { mutableStateOf(false) }
    val interstitialController = rememberInterstitialAd {
        setStoragePermission = getStoragePermission()
    }

    LaunchedEffect(downloadList) {
        downloadList
            ?.filter { it.status == SUCCESS }
            ?.forEach { syncToGallery(context, it.fileName) }
    }

    LaunchedEffect(urlIntent) {
        if (urlIntent.isNotEmpty()) {
            urlSocialMediaState = TextFieldValue(urlIntent)
            resetUrlIntent()
            checkStateBeforeDownload(
                urlSocialMediaState = urlSocialMediaState,
                storageInfo = storageInfo,
                onActionStorageFull = {
                    isStorageFull = true
                    errorMessage = context.getString(R.string.storage_full)
                },
                onActionIsContentPlaylist = {
                    errorMessage = context.getString(R.string.playlist_not_supported)
                },
                onActionIsContentYT = {
                    if (youtubeResolutions.isNotEmpty()) showYoutubeChooseQualityModal = true
                    else showUnsupportedYouTubeDialog = true
                },
                onActionDownload = { getApiDownloader() }
            )
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
        if (shouldShowDonationOfferDialog) {
            (0..3).random(Random).also { randomValue ->
                randomDonateDialogOffer = randomValue
                shouldShowDonationOfferDialog = false
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch(IO) { storageInfo = getStorageInfo(context) }
    }

    if (setStoragePermission.isNotEmpty()) {
        MeverPermissionHandler(
            permissions = setStoragePermission,
            onGranted = {
                setStoragePermission = emptyList()
                checkStateBeforeDownload(
                    urlSocialMediaState = urlSocialMediaState,
                    storageInfo = storageInfo,
                    onActionStorageFull = {
                        isStorageFull = true
                        errorMessage = context.getString(R.string.storage_full)
                    },
                    onActionIsContentPlaylist = {
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

    BackHandler(showLoading) { showCancelExitConfirmation = true }

    HandleBottomSheetDownload(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
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
                        urls.map { url ->
                            async(IO) {
                                semaphore.withPermit {
                                    runCatching {
                                        val content = byUrl[url] ?: return@runCatching
                                        val fileName = content.fileName.ifEmpty {
                                            changeToCurrentDate(currentTimeMillis()) + getExtensionFromUrl(
                                                url = url,
                                                extensionFromResponse = content.type
                                            )
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
                                primaryContent = content.url,
                                fileName = content.fileName
                            )
                        }
                    }

                    if (loadingItemIndex == index) {
                        isInPreview = true
                        delay(150)
                        navController.navigateTo(
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

    HandleDialogUnsupportedYt(showUnsupportedYouTubeDialog) { showUnsupportedYouTubeDialog = false }

    HandleDialogExitConfirmation(
        showDialog = showCancelExitConfirmation,
        onClickPrimary = { activity.finish() },
        onClickSecondary = { showCancelExitConfirmation = false }
    )

    HandleDonationDialogOffer(
        showDialog = randomDonateDialogOffer == 1,
        onClickPrimaryButton = {
            randomDonateDialogOffer = 0
            navController.navigateTo(SettingAppreciateRoute)
        },
        onClickSecondaryButton = { randomDonateDialogOffer = 0 }
    )

    MeverDialogError(
        showDialog = errorMessage.isNotEmpty(),
        errorTitle = stringResource(R.string.error_title),
        errorDescription = errorMessage,
        primaryButtonText = stringResource(if (isStorageFull) R.string.ok else R.string.retry),
        onClickPrimary = {
            if (isStorageFull) isStorageFull = false else getApiDownloader()
            errorMessage = ""
        },
        onClickSecondary = {
            isStorageFull = false
            errorMessage = ""
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

    showDeleteDialog?.let { id ->
        MeverDialogError(
            showDialog = true,
            errorImage = null,
            errorTitle = stringResource(R.string.delete_title),
            errorDescription = stringResource(R.string.delete_desc),
            primaryButtonText = stringResource(R.string.delete_button),
            onClickPrimary = {
                delete(id)
                showDeleteDialog = null
            },
            onClickSecondary = { showDeleteDialog = null },
        )
    }

    showFailedDialog?.let { id ->
        MeverDialogError(
            showDialog = true,
            errorTitle = stringResource(R.string.download_failed_title),
            errorDescription = stringResource(R.string.download_failed_desc),
            primaryButtonText = stringResource(R.string.delete_button),
            secondaryButtonText = stringResource(R.string.retry),
            onClickPrimary = {
                delete(id)
                showFailedDialog = null
            },
            onClickSecondary = {
                retryDownload(id)
                showFailedDialog = null
            }
        )
    }

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        LazyColumn(modifier = modifier) {
            if (isImageGeneratorFeatureActive.not() && isPhoneDevice) item {
                Spacer(modifier = Modifier.size(Dp32))
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.downloader_title),
                    textAlign = if (isImageGeneratorFeatureActive.not() && isPhoneDevice) {
                        TextAlign.Center
                    } else TextAlign.Start,
                    style = typography.h2.copy(fontSize = if (isImageGeneratorFeatureActive) Sp22 else Sp26),
                    color = colorScheme.onPrimary
                )
            }
            item {
                Spacer(modifier = Modifier.size(Dp16))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.downloader_desc),
                    textAlign = if (isImageGeneratorFeatureActive.not() && isPhoneDevice) {
                        TextAlign.Center
                    } else TextAlign.Start,
                    style = typography.body2,
                    color = colorScheme.secondary
                )
            }
            if (isImageGeneratorFeatureActive || isPhoneDevice.not()) item {
                Spacer(modifier = Modifier.size(Dp24))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(Dp16)
                ) {
                    Row(horizontalArrangement = spacedBy((-Dp20))) {
                        val platforms = PlatformType.entries.filter {
                            it in listOf(FACEBOOK, INSTAGRAM, TIKTOK, TWITTER, PINTEREST)
                        }
                        platforms.forEach { type ->
                            MeverIcon(
                                icon = getPlatformIcon(type.platformName),
                                iconBackgroundColor = getPlatformIconBackgroundColor(type.platformName),
                                iconSize = Dp48,
                                iconPadding = Dp10
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MeverLightViolet,
                                    shape = CircleShape
                                )
                                .size(Dp48),
                            contentAlignment = Center
                        ) {
                            Text(
                                text = if (youtubeResolutions.isNotEmpty()) "+8" else "+7",
                                textAlign = TextAlign.Center,
                                style = typography.bodyBold1,
                                color = MeverPurple
                            )
                        }
                    }
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dp8))
                            .onCustomClick { showPlatformSupportDialog = true },
                        text = stringResource(R.string.see_all_supported_platforms),
                        maxLines = 2,
                        overflow = Ellipsis,
                        style = typography.bodyBold1,
                        color = MeverPurple
                    )
                }
            }
            if (isImageGeneratorFeatureActive || isPhoneDevice.not()) {
                item {
                    Spacer(modifier = Modifier.size(Dp24))
                    MeverTextField(
                        modifier = Modifier.fillMaxWidth(),
                        context = context,
                        value = urlSocialMediaState,
                        onValueChange = { urlSocialMediaState = it }
                    )
                    Spacer(modifier = Modifier.size(Dp10))
                    MeverButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp40),
                        title = stringResource(R.string.download),
                        buttonType = Filled(
                            backgroundColor = colorScheme.primary,
                            contentColor = MeverWhite
                        ),
                        isEnabled = getPlatformType(
                            urlSocialMediaState.text.trim()
                        ) != ALL && showLoading.not(),
                        isLoading = showLoading
                    ) {
                        handleClickButton(
                            buttonClickCount = getButtonClickCount,
                            onIncrementClickCount = { incrementClickCount() },
                            onShowAds = { interstitialController.showAd() },
                            onClickAction = {
                                if (showLoading.not()) setStoragePermission = getStoragePermission()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.size(Dp24))
                }
            } else item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dp24),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(Dp8)
                ) {
                    MeverTextField(
                        modifier = Modifier.weight(1f),
                        context = context,
                        value = urlSocialMediaState,
                        onValueChange = { urlSocialMediaState = it }
                    )
                    MeverButton(
                        modifier = Modifier.size(Dp48),
                        title = "",
                        buttonType = Filled(
                            backgroundColor = colorScheme.primary,
                            contentColor = MeverWhite
                        ),
                        shape = CircleShape,
                        isEnabled = getPlatformType(
                            urlSocialMediaState.text.trim()
                        ) != ALL && showLoading.not(),
                        isLoading = showLoading
                    ) {
                        handleClickButton(
                            buttonClickCount = getButtonClickCount,
                            onIncrementClickCount = { incrementClickCount() },
                            onShowAds = { interstitialController.showAd() },
                            onClickAction = {
                                if (showLoading.not()) setStoragePermission =
                                    getStoragePermission()
                            }
                        )
                    }
                }
                if (isExploreImageFeatureActive) {
                    BannerExploreImage(isPhoneDevice) { navController.navigateTo(ExploreLandingRoute) }
                    Spacer(modifier = Modifier.size(Dp8))
                }
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
                            .onCustomClick { navController.navigateToGalleryScreen() }
                    )
                }
            }
            downloadList?.let { files ->
                if (files.isNotEmpty()) {
                    items(
                        items = files.toMutableStateList().apply {
                            if (size > 3) removeRange(3, size)
                        },
                        key = { it.id },
                        contentType = { it.status.name }
                    ) { model ->
                        MeverCard(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dp12))
                                .animateItem(),
                            paddingValues = PaddingValues(vertical = Dp24),
                            cardArgs = MeverCardArgs(
                                source = model.url,
                                tag = model.tag,
                                fileName = model.fileName,
                                status = model.status,
                                progress = model.progress,
                                total = model.total,
                                path = model.path,
                                urlThumbnail = model.metaData,
                                icon = getPlatformIcon(model.tag),
                                iconBackgroundColor = getPlatformIconBackgroundColor(model.tag),
                                iconSize = Dp24,
                                iconPadding = Dp5
                            ),
                            onClickCard = {
                                with(model) {
                                    when (status) {
                                        SUCCESS -> {
                                            if (isMusic(model.fileName).not()) {
                                                navController.navigateTo(
                                                    GalleryContentDetailRoute(
                                                        contents = downloadList
                                                            .filterNot { isMusic(it.fileName) }
                                                            .map {
                                                                Content(
                                                                    id = it.id,
                                                                    isVideo = isVideo(it.path),
                                                                    primaryContent = it.path,
                                                                    fileName = it.fileName
                                                                )
                                                            },
                                                        initialIndex = downloadList.filterNot {
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
                            },
                            onClickLong = { showDeleteDialog = model.id },
                            onClickShare = {
                                shareContent(
                                    context = context,
                                    file = File(model.path)
                                )
                            },
                            onClickDelete = { showDeleteDialog = model.id }
                        )
                    }
                    item { Spacer(modifier = Modifier.size(Dp24)) }
                } else item {
                    MeverEmptyItem(
                        modifier = Modifier.padding(top = Dp32),
                        image = R.drawable.ic_not_found,
                        size = Dp150.plus(Dp16),
                        description = stringResource(R.string.empty_list_desc)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeAiSection(
    context: Context,
    prompt: String,
    deviceType: DeviceType,
    totalImageSelected: Int,
    artStyleSelected: String,
    modifier: Modifier = Modifier,
    onPromptChange: (String) -> Unit,
    onImageCountSelected: (Int) -> Unit,
    onArtStyleSelected: (String, String) -> Unit,
    onClickGenerate: (() -> Unit)? = null
) = CompositionLocalProvider(LocalOverscrollFactory provides null) {
    val imagesCountGenerated = remember { List(4) { it + 1 } }
    val artStyles = remember { getArtStyles(context) }

    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = Dp16),
                    text = stringResource(R.string.image_generator),
                    style = typography.h2.copy(fontSize = Sp22),
                    color = colorScheme.onPrimary
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(bottom = Dp24),
                    text = stringResource(R.string.image_generator_desc),
                    style = typography.body2,
                    color = colorScheme.secondary
                )
            }
            item {
                MeverAutoSizableTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp150),
                    value = prompt,
                    fontSize = Sp18,
                    minFontSize = Sp14,
                    maxLines = 4,
                    onClickInspire = { onPromptChange(getInspirePrompt()) },
                    onValueChange = { onPromptChange(it) }
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = Dp24),
                    text = stringResource(R.string.total_images),
                    style = typography.bodyBold1,
                    color = colorScheme.onPrimary
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = spacedBy(Dp8)
                ) {
                    imagesCountGenerated.forEach { count ->
                        MeverButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(Dp40),
                            title = count.toString(),
                            buttonType = if (totalImageSelected == count) Filled(
                                backgroundColor = colorScheme.primary,
                                contentColor = MeverWhite
                            ) else Outlined(
                                borderColor = colorScheme.primary,
                                contentColor = colorScheme.primary
                            ),
                            shape = RoundedCornerShape(Dp12)
                        ) { if (totalImageSelected != count) onImageCountSelected(count) }
                    }
                }
            }
            item {
                Row(modifier = Modifier.padding(vertical = Dp24)) {
                    Text(
                        text = stringResource(R.string.art_style),
                        style = typography.bodyBold1,
                        color = colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.size(Dp4))
                    Text(
                        text = stringResource(R.string.optional),
                        style = typography.body2,
                        color = colorScheme.onPrimary
                    )
                    if (artStyleSelected.isNotEmpty()) Box(modifier = Modifier.weight(1f)) {
                        Text(
                            modifier = Modifier
                                .align(CenterEnd)
                                .clip(RoundedCornerShape(Dp12))
                                .onCustomClick { onArtStyleSelected("", "") },
                            text = stringResource(R.string.clear),
                            style = typography.bodyBold2,
                            color = colorScheme.primary
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = when (deviceType) {
                        TABLET -> SpaceAround
                        DESKTOP -> SpaceEvenly
                        else -> SpaceBetween
                    },
                    verticalAlignment = CenterVertically
                ) {
                    artStyles.forEach {
                        val imageSize by animateDpAsState(
                            targetValue = if (artStyleSelected == it.styleName) Dp80 else Dp75
                        )
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = spacedBy(Dp4)
                        ) {
                            Box(
                                modifier = Modifier.size(Dp80),
                                contentAlignment = Center
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(imageSize)
                                        .clip(RoundedCornerShape(Dp12))
                                        .then(
                                            if (artStyleSelected == it.styleName) Modifier
                                                .border(
                                                    width = Dp4,
                                                    color = colorScheme.primary,
                                                    shape = RoundedCornerShape(Dp12)
                                                )
                                            else Modifier
                                        )
                                        .onCustomClick {
                                            if (artStyleSelected != it.styleName) {
                                                onArtStyleSelected(it.styleName, it.promptKeywords)
                                            }
                                        },
                                    painter = painterResource(it.image),
                                    contentScale = Crop,
                                    contentDescription = it.styleName
                                )
                            }
                            Text(
                                text = it.styleName,
                                style = typography.bodyBold3,
                                color = colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BannerExploreImage(isPhoneDevice: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dp52)
            .background(color = colorScheme.primary, shape = RoundedCornerShape(Dp12))
            .clip(RoundedCornerShape(Dp12))
            .onCustomClick { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.find_image),
                style = if (isPhoneDevice) typography.bodyBold1 else typography.bodyBold2,
                color = MeverWhite
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_started),
                tint = MeverYellow,
                contentDescription = "Arrow Right"
            )
        }
    }
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
    context.getString(R.string.explore) to FeatureHomeR.drawable.ic_explore,
    context.getString(R.string.gallery) to if (hasDownloadProgress) FeatureHomeR.drawable.ic_notification
    else FeatureHomeR.drawable.ic_gallery,
    context.getString(R.string.settings) to FeatureHomeR.drawable.ic_setting
)

private fun tabItems(context: Context) = listOf(
    context.getString(R.string.downloader_tab),
    context.getString(R.string.ai_tab)
)

private fun NavController.handleClickActionMenu(context: Context, name: String) = when (name) {
    context.getString(R.string.explore) -> navigateTo(ExploreLandingRoute)
    context.getString(R.string.gallery) -> navigateToGalleryScreen()
    context.getString(R.string.settings) -> navigateToSettingScreen()
    else -> Unit
}

private fun NavController.navigateToGalleryScreen() = navigateTo(GalleryLandingRoute)

private fun NavController.navigateToSettingScreen() = navigateTo(SettingLandingRoute)

private fun NavController.navigateToImageGenerator(
    prompt: String,
    artStyle: String,
    totalImages: Int
) = navigateTo(
    HomeImageGeneratorResultRoute(
        prompt = prompt,
        artStyle = artStyle,
        totalImages = totalImages
    )
)