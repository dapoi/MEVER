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
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.dapascript.mever.core.common.ui.component.MeverDialogError
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverIcon
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.MeverTabs
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp75
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.NETWORK
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.RESPONSE
import com.dapascript.mever.core.common.util.ErrorHandle.getErrorResponseContent
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Available
import com.dapascript.mever.core.common.util.getFilePath
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAppreciateRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getArtStyles
import com.dapascript.mever.feature.home.screen.attr.HomeLandingScreenAttr.getInspirePrompt
import com.dapascript.mever.feature.home.screen.component.HandleBottomSheetDownload
import com.dapascript.mever.feature.home.screen.component.HandleDialogExitConfirmation
import com.dapascript.mever.feature.home.screen.component.HandleDialogYoutubeQuality
import com.dapascript.mever.feature.home.screen.component.HandleDonationDialogOffer
import com.dapascript.mever.feature.home.screen.component.HandleHomeDialogPermission
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel
import com.ketch.DownloadModel
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.SUCCESS
import kotlinx.coroutines.launch
import java.io.File
import java.lang.System.currentTimeMillis
import kotlin.random.Random
import com.dapascript.mever.feature.home.R as FeatureHomeR

@Composable
internal fun HomeLandingScreen(
    navController: NavController,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val downloaderResponseState = downloaderResponseState.collectAsStateValue()
    val isNetworkAvailable = isNetworkAvailable.collectAsStateValue()
    val isImageGeneratorFeatureActive = isImageGeneratorFeatureActive.collectAsStateValue()
    val youtubeResolutions = youtubeResolutions.collectAsStateValue()
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    var showLoading by remember { mutableStateOf(false) }
    var showCancelExitConfirmation by remember { mutableStateOf(false) }
    var showYoutubeChooseQualityModal by remember { mutableStateOf(false) }
    var showErrorModal by remember { mutableStateOf<ErrorType?>(null) }
    var randomDonateDialogOffer by remember { mutableIntStateOf(0) }
    var setStoragePermission by remember { mutableStateOf<List<String>>(emptyList()) }

    BaseScreen(
        useSystemBarsPadding = true,
        allowScreenOverlap = true,
        hideDefaultTopBar = true
    ) {
        if (setStoragePermission.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = setStoragePermission,
                onGranted = {
                    setStoragePermission = emptyList()
                    getNetworkStatus(
                        isNetworkAvailable = isNetworkAvailable,
                        onNetworkAvailable = {
                            if (getPlatformType(urlSocialMediaState.text) == YOUTUBE) {
                                showYoutubeChooseQualityModal = true
                            } else getApiDownloader()
                        },
                        onNetworkUnavailable = { showErrorModal = NETWORK }
                    )
                },
                onDenied = { isPermanentlyDeclined, retry ->
                    HandleHomeDialogPermission(
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

        LaunchedEffect(downloaderResponseState) {
            downloaderResponseState.handleUiState(
                onLoading = { showLoading = true },
                onSuccess = { showLoading = false },
                onFailed = {
                    showLoading = false
                    showErrorModal = RESPONSE
                }
            )
        }

        LaunchedEffect(randomDonateDialogOffer, showDonationDialog) {
            if (showDonationDialog) {
                (0..5).random(Random).also { randomValue ->
                    randomDonateDialogOffer = randomValue
                    showDonationDialog = false
                }
            }
        }

        HandleBottomSheetDownload(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            listContent = contents,
            showBottomSheet = contents.isNotEmpty(),
            isFailedFetchImage = isNetworkAvailable != Available,
            onClickDownload = { url ->
                scope.launch {
                    startDownload(
                        url = url,
                        fileName = changeToCurrentDate(currentTimeMillis()) + getUrlContentType(url),
                        thumbnail = contents.firstOrNull()?.thumbnail.orEmpty()
                    )
                    contents = emptyList()
                }
            },
            onClickDismiss = { contents = emptyList() }
        )

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

        getErrorResponseContent(showErrorModal)?.let { (title, desc) ->
            MeverDialogError(
                showDialog = true,
                errorTitle = stringResource(title),
                errorDescription = stringResource(desc),
                onClickPrimary = {
                    showErrorModal = null
                    getNetworkStatus(
                        isNetworkAvailable = isNetworkAvailable,
                        onNetworkAvailable = { getApiDownloader() },
                        onNetworkUnavailable = { showErrorModal = NETWORK }
                    )
                },
                onClickSecondary = { showErrorModal = null }
            )
        }

        HandleDialogYoutubeQuality(
            showDialog = showYoutubeChooseQualityModal,
            qualityList = youtubeResolutions,
            onApplyQuality = { quality ->
                showYoutubeChooseQualityModal = false
                selectedQuality = quality
                getApiDownloader()
            },
            onDismiss = { showYoutubeChooseQualityModal = false }
        )

        HomeScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            viewModel = this,
            navController = navController,
            isImageGeneratorFeatureActive = isImageGeneratorFeatureActive,
            isLoading = showLoading
        ) { setStoragePermission = getStoragePermission }
    }
}

@Composable
private fun HomeScreenContent(
    viewModel: HomeLandingViewModel,
    navController: NavController,
    isLoading: Boolean,
    isImageGeneratorFeatureActive: Boolean,
    modifier: Modifier = Modifier,
    requestStoragePermissionLauncher: () -> Unit
) = with(viewModel) {
    BoxWithConstraints(modifier = modifier) {
        val context = LocalContext.current
        val downloadList = downloadList.collectAsStateValue()
        val getListActionMenu = remember { getListActionMenu(context) }
        val tabItems = remember { tabItems(context) }
        val pagerState = rememberPagerState(pageCount = { tabItems.size })
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
        var showFailedDialog by remember { mutableStateOf<Int?>(null) }
        var generateButtonHeight by remember { mutableIntStateOf(0) }

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
                    title = null,
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
                                    context = context,
                                    downloadList = downloadList,
                                    isLoading = isLoading,
                                    urlSocialMediaState = urlSocialMediaState,
                                    onClickCard = { model ->
                                        with(model) {
                                            when (status) {
                                                SUCCESS -> navController.navigateTo(
                                                    GalleryContentDetailRoute(
                                                        id = id,
                                                        filePath = getFilePath(fileName)
                                                    )
                                                )

                                                FAILED -> showFailedDialog = id
                                                PAUSED -> resumeDownload(id)
                                                else -> pauseDownload(id)
                                            }
                                        }
                                    },
                                    onClickDelete = { showDeleteDialog = it.id },
                                    onClickShare = {
                                        shareContent(
                                            context = context,
                                            file = File(getFilePath(it.fileName))
                                        )
                                    },
                                    onValueChange = { urlSocialMediaState = it },
                                    onClickDownload = {
                                        if (isLoading.not()) requestStoragePermissionLauncher()
                                    },
                                    onClickViewAll = { navController.navigateToGalleryScreen() }
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
            }
        }
        AnimatedVisibility(
            modifier = Modifier.align(BottomCenter),
            visible = pagerState.currentPage == 1,
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
                    navController.navigateTo(
                        HomeImageGeneratorResultRoute(
                            prompt = promptState.text,
                            artStyle = selectedArtStyle.second,
                            totalImages = selectedImageCount
                        )
                    )
                }
            }
        }

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
    }
}

@Composable
internal fun HomeDownloaderSection(
    context: Context,
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
) = CompositionLocalProvider(LocalOverscrollFactory provides null) {
    LazyColumn(modifier = modifier) {
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
                PlatformType.entries.filter {
                    it.platformName !in listOf(AI.platformName, ALL.platformName)
                }.map {
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
                context = context,
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
                buttonType = Filled(
                    backgroundColor = colorScheme.primary,
                    contentColor = MeverWhite
                ),
                isEnabled = getPlatformType(urlSocialMediaState.text.trim()) != ALL,
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
            if (files.isNotEmpty()) {
                items(
                    items = files.toMutableStateList().apply { if (size > 5) removeRange(5, size) },
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
                            icon = if (it.tag.isNotEmpty() && it.tag != AI.platformName) {
                                getPlatformIcon(it.tag)
                            } else null,
                            iconBackgroundColor = getPlatformIconBackgroundColor(it.tag),
                            iconSize = Dp24,
                            iconPadding = Dp5
                        ),
                        onClickCard = { onClickCard(it) },
                        onClickShare = { onClickShare(it) },
                        onClickDelete = { onClickDelete(it) }
                    )
                }
                item { Spacer(modifier = Modifier.size(Dp40)) }
            } else item {
                MeverEmptyItem(
                    image = R.drawable.ic_not_found,
                    size = Dp150.plus(Dp16),
                    description = stringResource(R.string.empty_list_desc)
                )
            }
        }
    }
}

@Composable
internal fun HomeAiSection(
    context: Context,
    prompt: String,
    totalImageSelected: Int,
    artStyleSelected: String,
    modifier: Modifier = Modifier,
    onPromptChange: (String) -> Unit,
    onImageCountSelected: (Int) -> Unit,
    onArtStyleSelected: (String, String) -> Unit
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
                    imagesCountGenerated.map { count ->
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
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    artStyles.map {
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
        MeverBannerAd(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dp8)
                .clipToBounds()
        )
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