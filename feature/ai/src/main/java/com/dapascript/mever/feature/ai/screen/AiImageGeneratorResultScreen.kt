package com.dapascript.mever.feature.ai.screen

import android.content.Context
import android.graphics.Bitmap.CompressFormat.PNG
import android.os.Handler
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverAutoSizableTextField
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDeclinedPermissionDialog
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.core.common.util.fetchPhotoFromUrl
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.navigateToAppSettings
import com.dapascript.mever.core.common.util.onClickWithAds
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.storage.StorageUtil.getStorageInfo
import com.dapascript.mever.core.common.util.storage.StorageUtil.isStorageFull
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageGeneratorLandingRoute
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageGeneratorResultRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.ai.screen.attr.AiImageGeneratorResultAttr.getMenuActions
import com.dapascript.mever.feature.ai.viewmodel.AiImageGeneratorResultViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
internal fun AiImageGeneratorResultScreen(
    navigator: Navigator,
    args: AiImageGeneratorResultRoute,
    viewModel: AiImageGeneratorResultViewModel = hiltViewModel()
) = with(viewModel) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val resources = LocalResources.current
    val deviceType = LocalDeviceType.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val aiResponseState = aiResponseState.collectAsStateValue()
    val aiReportState = aiReportState.collectAsStateValue()
    val getButtonClickCount = getButtonClickCount.collectAsStateValue()
    val adsThreshold = adsThreshold.collectAsStateValue()
    var hasCopied by remember { mutableStateOf(false) }
    var showShimmer by rememberSaveable { mutableStateOf(true) }
    var showLoadingReport by remember { mutableStateOf(false) }
    var showCancelExitConfirmation by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var checkStoragePermission by remember { mutableStateOf<List<String>>(emptyList()) }
    val interstitialAd = rememberInterstitialAd {
        imageResult = null
        showShimmer = true
        getImageAiGenerator(args.prompt, args.artStyle)
    }

    BaseScreen(
        topBarArgs = TopBarArgs(title = stringResource(R.string.ai_image_generator)),
        useNavigationBarsPadding = true,
        onBackHandler = {
            if (showShimmer) showCancelExitConfirmation = true else navigator.navigateBack()
        }
    ) {
        LaunchedEffect(imageResult) {
            if (imageResult == null) getImageAiGenerator(args.prompt, args.artStyle)
        }

        LaunchedEffect(aiResponseState) {
            aiResponseState.handleUiState(
                onSuccess = { result ->
                    showShimmer = false
                    imageResult = result
                },
                onFailed = { message ->
                    showShimmer = false
                    imageResult = null
                    errorMessage = message ?: resources.getString(R.string.error_desc)
                }
            )
        }

        LaunchedEffect(aiReportState) {
            aiReportState.handleUiState(
                onLoading = { showLoadingReport = true },
                onSuccess = {
                    showLoadingReport = false
                    showReportDialog = false
                    navigator.navigateBack()
                },
                onFailed = { message ->
                    showLoadingReport = false
                    showReportDialog = false
                    errorMessage = message ?: resources.getString(R.string.error_desc)
                }
            )
        }

        LaunchedEffect(navigator) {
            navigator.navResult.collect {
                startDownload(
                    url = imageResult?.imagesUrl.orEmpty(),
                    fileName = imageResult?.fileName.orEmpty()
                )
                navigator.navigate(
                    route = GalleryLandingRoute,
                    popUpTo = AiImageGeneratorLandingRoute,
                    isInclusive = true
                )
            }
        }

        BackHandler { showCancelExitConfirmation = true }

        if (checkStoragePermission.isNotEmpty()) {
            val storageInfo = remember { getStorageInfo(context) }
            MeverPermissionHandler(
                permissions = checkStoragePermission,
                onGranted = {
                    checkStoragePermission = emptyList()
                    if (isStorageFull(storageInfo)) {
                        errorMessage = resources.getString(R.string.storage_full)
                    } else {
                        startDownload(
                            url = imageResult?.imagesUrl.orEmpty(),
                            fileName = imageResult?.fileName.orEmpty()
                        )
                        navigator.navigate(
                            route = GalleryLandingRoute,
                            popUpTo = AiImageGeneratorLandingRoute,
                            isInclusive = true
                        )
                    }
                },
                onDenied = { isPermanentlyDeclined, retry ->
                    MeverDeclinedPermissionDialog(
                        isPermissionsDeclined = isPermanentlyDeclined,
                        onGoToSetting = {
                            checkStoragePermission = emptyList()
                            navigateToAppSettings(activity)
                        },
                        onRetry = { retry() },
                        onDismiss = { checkStoragePermission = emptyList() }
                    )
                }
            )
        }

        MeverDialog(
            showDialog = showCancelExitConfirmation,
            image = null,
            title = stringResource(R.string.cancel_fetch_title),
            description = stringResource(R.string.cancel_fetch_desc),
            primaryActionLabel = stringResource(R.string.yes),
            onClickPrimaryAction = { navigator.navigateBack() },
            onClickSecondaryAction = { showCancelExitConfirmation = false }
        )

        MeverDialog(
            showDialog = errorMessage.isNotEmpty(),
            description = errorMessage,
            onClickPrimaryAction = {
                errorMessage = ""
                imageResult = null
                showShimmer = true
                getImageAiGenerator(args.prompt, args.artStyle)
            },
            onClickSecondaryAction = { navigator.navigateBack() }
        )

        MeverBottomSheet(
            showBottomSheet = showReportDialog,
            shouldDismissOnClickOutside = true,
            onDismissBottomSheet = { showReportDialog = false }
        ) {
            var reportMessage by remember { mutableStateOf("") }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dp24, end = Dp24, bottom = Dp16),
                verticalArrangement = spacedBy(Dp16)
            ) {
                Text(
                    text = stringResource(R.string.prompt),
                    style = typography.bodyBold1,
                    color = colors.blackWhite
                )
                MeverAutoSizableTextField(
                    value = args.prompt,
                    isReadOnly = true,
                    fontSize = Sp18,
                    minFontSize = Sp14
                ) {}
                Text(
                    text = stringResource(R.string.report_desc),
                    style = typography.bodyBold1,
                    color = colors.blackWhite
                )
                MeverAutoSizableTextField(
                    heightFreeTextContainer = Dp150,
                    value = reportMessage,
                    fontSize = Sp18,
                    minFontSize = Sp14,
                    maxLines = 6
                ) { reportMessage = it }
                MeverButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp52),
                    title = stringResource(R.string.submit),
                    isEnabled = reportMessage.isNotBlank(),
                    isLoading = showLoadingReport,
                    buttonType = Filled(
                        backgroundColor = colors.alwaysPurple,
                        contentColor = MeverWhite
                    )
                ) { postReportAiImage(reportMessage) }
            }
        }

        AnimatedContent(
            targetState = showShimmer && imageResult?.imagesUrl.isNullOrEmpty(),
            transitionSpec = { (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false)) }
        ) { isLoading ->
            if (isLoading) ImageGeneratorLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp64, start = Dp24, end = Dp24)
                    .verticalScroll(scrollState),
                deviceType = deviceType
            ) else ImageGeneratorResultContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp64),
                context = context,
                deviceType = deviceType,
                urlImage = imageResult?.imagesUrl.orEmpty(),
                promptText = args.prompt,
                hasCopied = hasCopied,
                scrollState = scrollState,
                onClickReport = { showReportDialog = true },
                onClickCopy = {
                    copyToClipboard(context, args.prompt)
                    hasCopied = true
                },
                onClickShare = {
                    scope.launch {
                        val cachePath = File(context.cacheDir, "images")
                        if (!cachePath.exists()) cachePath.mkdirs()
                        val cacheFile = File(cachePath, "shared_image.png")
                        val bitmap = fetchPhotoFromUrl(imageResult?.imagesUrl.orEmpty())

                        bitmap?.let {
                            val stream = FileOutputStream(cacheFile)
                            it.compress(PNG, 100, stream)
                            stream.close()

                            shareContent(
                                context = context,
                                contentPath = cacheFile.absolutePath,
                                isCache = true
                            )

                            Handler(context.mainLooper).postDelayed({
                                if (cacheFile.exists()) cacheFile.delete()
                            }, 5000)
                        }
                    }
                },
                onClickRegenerate = {
                    onClickWithAds(
                        buttonClickCount = getButtonClickCount,
                        adsThreshold = adsThreshold,
                        onIncrementClickCount = { incrementClickCount() },
                        onShowAds = { interstitialAd.showAd() },
                        onClickAction = {
                            imageResult = null
                            showShimmer = true
                            getImageAiGenerator(args.prompt, args.artStyle)
                        }
                    )
                },
                onClickDownload = { checkStoragePermission = getStoragePermission() },
                onClickImage = {
                    navigator.navigate(
                        GalleryContentDetailRoute(
                            contents = listOf(
                                Content(
                                    id = 0,
                                    isVideo = false,
                                    fileName = imageResult?.fileName.orEmpty(),
                                    media = imageResult?.imagesUrl.orEmpty(),
                                    isDownloadable = true,
                                    isPreview = true,
                                    isDeletable = false
                                )
                            ),
                            initialIndex = 0
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun ImageGeneratorResultContent(
    context: Context,
    deviceType: DeviceType,
    urlImage: String,
    promptText: String,
    hasCopied: Boolean,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    onClickReport: () -> Unit,
    onClickCopy: () -> Unit,
    onClickShare: () -> Unit,
    onClickRegenerate: () -> Unit,
    onClickDownload: () -> Unit,
    onClickImage: () -> Unit
) = Box(modifier = modifier) {
    if (deviceType == PHONE) Column(
        modifier = Modifier
            .matchParentSize()
            .padding(horizontal = Dp24)
            .navigationBarsPadding()
            .verticalScroll(scrollState),
        verticalArrangement = spacedBy(Dp16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dp12))
                .onCustomClick(onClick = onClickImage)
        ) {
            MeverImage(
                modifier = Modifier.fillMaxSize(),
                source = urlImage,
                contentScale = FillBounds
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dp16),
                contentAlignment = TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(Dp32)
                        .background(
                            color = colors.blackWhite.copy(alpha = 0.4f),
                            shape = CircleShape
                        ),
                    contentAlignment = Center
                ) {
                    Icon(
                        modifier = Modifier.size(Dp20),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_fullscreen),
                        tint = colors.alwaysWhite,
                        contentDescription = null
                    )
                }
            }
        }
        Text(
            text = stringResource(R.string.prompt),
            style = typography.bodyBold1,
            color = colors.blackWhite
        )
        MeverAutoSizableTextField(
            heightFreeTextContainer = Dp150,
            value = promptText,
            isReadOnly = true,
            fontSize = Sp18,
            minFontSize = Sp14,
            maxLines = 4
        ) {}
        getMenuActions(context, hasCopied).forEach { (title, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
                    .background(color = colors.whiteDarkGray, shape = RoundedCornerShape(Dp12))
                    .clip(RoundedCornerShape(Dp12))
                    .onCustomClick {
                        when (icon) {
                            R.drawable.ic_report -> onClickReport()
                            R.drawable.ic_copy -> onClickCopy()
                            R.drawable.ic_share -> onClickShare()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier.padding(Dp10),
                    horizontalArrangement = spacedBy(Dp8),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(Dp20),
                        imageVector = ImageVector.vectorResource(icon),
                        tint = colors.blackWhite.copy(alpha = 0.4f),
                        contentDescription = "Copy Prompt"
                    )
                    Text(
                        text = title,
                        style = typography.bodyBold2,
                        color = colors.blackWhite
                    )
                }
            }
        }
        MeverBannerAd(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(Dp120))
    } else Row(
        modifier = Modifier
            .matchParentSize()
            .padding(horizontal = Dp24)
            .navigationBarsPadding()
            .verticalScroll(scrollState),
        horizontalArrangement = spacedBy(Dp16)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dp12))
                .graphicsLayer {
                    scaleY = 1.1f
                    scaleX = 1.1f
                    clip = true
                }
                .onCustomClick(onClick = onClickImage)
        ) {
            MeverImage(
                modifier = Modifier.fillMaxSize(),
                source = urlImage
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dp16),
                contentAlignment = TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(Dp32)
                        .background(
                            color = colors.blackWhite.copy(alpha = 0.4f),
                            shape = CircleShape
                        ),
                    contentAlignment = Center
                ) {
                    Icon(
                        modifier = Modifier.size(Dp20),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_fullscreen),
                        tint = colors.alwaysWhite,
                        contentDescription = null
                    )
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = spacedBy(Dp16)
        ) {
            Text(
                text = stringResource(R.string.prompt),
                style = typography.bodyBold1,
                color = colors.blackWhite
            )
            MeverAutoSizableTextField(
                heightFreeTextContainer = Dp120,
                value = promptText,
                isReadOnly = true,
                fontSize = Sp18,
                minFontSize = Sp14,
                maxLines = 4
            ) {}
            Spacer(modifier = Modifier.weight(1f))
            getMenuActions(context, hasCopied).forEach { (title, icon) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
                        .background(
                            color = colors.whiteDarkGray,
                            shape = RoundedCornerShape(Dp12)
                        )
                        .clip(RoundedCornerShape(Dp12))
                        .onCustomClick {
                            when (icon) {
                                R.drawable.ic_copy -> onClickCopy()
                                R.drawable.ic_report -> onClickReport()
                                R.drawable.ic_share -> onClickShare()
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(Dp10),
                        horizontalArrangement = spacedBy(Dp8),
                        verticalAlignment = CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(Dp20),
                            imageVector = ImageVector.vectorResource(icon),
                            tint = colors.blackWhite.copy(alpha = 0.4f),
                            contentDescription = "Copy Prompt"
                        )
                        Text(
                            text = title,
                            style = typography.bodyBold2,
                            color = colors.blackWhite
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(BottomCenter)
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colors.whiteDark)
            ) {
                if (scrollState.canScrollBackward || scrollState.canScrollForward) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = Dp2,
                        color = colors.blackWhite.copy(alpha = 0.12f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dp24),
                    horizontalArrangement = spacedBy(Dp8),
                    verticalAlignment = CenterVertically
                ) {
                    MeverButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(Dp52),
                        title = stringResource(R.string.regenerate),
                        buttonType = Outlined(
                            borderColor = colors.alwaysPurple,
                            contentColor = colors.alwaysPurple
                        )
                    ) { onClickRegenerate() }
                    MeverButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(Dp52),
                        title = stringResource(R.string.download),
                        buttonType = Filled(
                            backgroundColor = colors.alwaysPurple,
                            contentColor = MeverWhite
                        )
                    ) { onClickDownload() }
                }
            }
        }
    }
}

@Composable
private fun ImageGeneratorLoading(
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) {
    if (deviceType == PHONE) Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dp12))
                .meverShimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp120)
                .clip(RoundedCornerShape(Dp12))
                .meverShimmer()
        )
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dp48)
                    .clip(RoundedCornerShape(Dp12))
                    .meverShimmer()
            )
        }
    } else Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp16)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(Dp12))
                    .meverShimmer()
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = spacedBy(Dp16)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp150)
                        .clip(RoundedCornerShape(Dp12))
                        .meverShimmer()
                )
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp48)
                            .clip(RoundedCornerShape(Dp12))
                            .meverShimmer()
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dp24)
                .align(BottomCenter),
            horizontalArrangement = spacedBy(Dp16)
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Dp52)
                        .clip(RoundedCornerShape(Dp12))
                        .meverShimmer()
                )
            }
        }
    }
}