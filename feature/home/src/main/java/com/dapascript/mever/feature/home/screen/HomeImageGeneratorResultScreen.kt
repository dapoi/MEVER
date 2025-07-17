package com.dapascript.mever.feature.home.screen

import android.content.Context
import android.graphics.Bitmap.CompressFormat.PNG
import android.os.Handler
import android.os.Looper.getMainLooper
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverAutoSizableTextField
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverSnackbar
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.NETWORK
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.RESPONSE
import com.dapascript.mever.core.common.util.ErrorHandle.getErrorResponseContent
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.getPhotoThumbnail
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.isAndroidTiramisuAbove
import com.dapascript.mever.core.common.util.navigateToGmail
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import com.dapascript.mever.feature.home.screen.attr.HomeImageGeneratorResultAttr.getMenuActions
import com.dapascript.mever.feature.home.screen.component.HandleDialogError
import com.dapascript.mever.feature.home.screen.component.HandleHomeDialogPermission
import com.dapascript.mever.feature.home.viewmodel.HomeImageGeneratorResultViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
internal fun HomeImageGeneratorResultScreen(
    navController: NavController,
    viewModel: HomeImageGeneratorResultViewModel = hiltViewModel()
) = with(viewModel) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val aiResponseState = aiResponseState.collectAsStateValue()
    val isNetworkAvailable = isNetworkAvailable.collectAsStateValue()
    var hasCopied by remember { mutableStateOf(false) }
    var aiImages by remember { mutableStateOf<List<String>>(emptyList()) }
    var showShimmer by remember { mutableStateOf(false) }
    var showErrorModal by remember { mutableStateOf<ErrorType?>(null) }
    var showCancelExitConfirmation by remember { mutableStateOf(false) }
    var isDownloadAllClicked by remember { mutableStateOf(false) }
    var imageSelected by remember(aiImages) { mutableStateOf(aiImages.firstOrNull()) }
    val snackbarMessage = remember { mutableStateOf("") }
    val storagePermLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) {
        val allGranted = getStoragePermission.all { permissions -> it[permissions] == true }
        if (allGranted) getNetworkStatus(
            isNetworkAvailable = isNetworkAvailable,
            onNetworkAvailable = {
                if (isDownloadAllClicked) {
                    aiImages.map { url ->
                        scope.launch {
                            startDownload(
                                url = url,
                                fileName = "${args.prompt}.jpg"
                            )
                        }
                    }
                    navController.navigateTo(
                        route = GalleryLandingRoute,
                        popUpTo = HomeImageGeneratorResultRoute::class,
                        inclusive = true
                    )
                } else {
                    snackbarMessage.value = context.getString(R.string.image_has_been_downloaded)
                    scope.launch {
                        startDownload(
                            url = imageSelected.orEmpty(),
                            fileName = args.prompt + getUrlContentType(imageSelected.orEmpty())
                        )
                    }
                    if (aiImages.size <= 1) navController.navigateTo(
                        route = GalleryLandingRoute,
                        popUpTo = HomeImageGeneratorResultRoute::class,
                        inclusive = true
                    ) else aiImages = aiImages.toMutableStateList().apply {
                        removeAt(aiImages.indexOf(imageSelected))
                    }
                }
            },
            onNetworkUnavailable = { showErrorModal = NETWORK }
        ) else getStoragePermission.forEach { permission ->
            onPermissionResult(permission, isGranted = it[permission] == true)
        }
    }
    val notifPermLauncher = rememberLauncherForActivityResult(RequestPermission()) {
        storagePermLauncher.launch(getStoragePermission)
    }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = stringResource(R.string.image_generator),
            onClickBack = { showCancelExitConfirmation = true }
        ),
        allowScreenOverlap = true
    ) {
        LaunchedEffect(isNetworkAvailable) {
            if (aiResponseState !is StateSuccess) getNetworkStatus(
                isNetworkAvailable = isNetworkAvailable,
                onNetworkAvailable = ::getImageAiGenerator,
                onNetworkUnavailable = { showErrorModal = NETWORK }
            )
        }

        LaunchedEffect(aiResponseState) {
            aiResponseState.handleUiState(
                onLoading = {
                    showShimmer = true
                    showErrorModal = null
                },
                onSuccess = { result ->
                    showShimmer = false
                    aiImages = result.imagesUrl.take(args.totalImages)
                },
                onFailed = {
                    showShimmer = false
                    aiImages = emptyList()
                    showErrorModal = RESPONSE
                }
            )
        }

        BackHandler { showCancelExitConfirmation = true }

        MeverDialog(
            showDialog = showCancelExitConfirmation,
            meverDialogArgs = MeverDialogArgs(
                title = stringResource(R.string.cancel_generate_title),
                onClickPrimaryButton = {
                    showCancelExitConfirmation = false
                    navController.popBackStack()
                },
                onClickSecondaryButton = { showCancelExitConfirmation = false }
            )
        ) {
            Text(
                text = stringResource(R.string.cancel_generate_desc),
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }

        HandleHomeDialogPermission(
            activity = activity,
            permission = showDialogPermission,
            onGoToSetting = {
                dismissDialog()
                activity.goToSetting()
            },
            onAllow = {
                dismissDialog()
                if (isAndroidTiramisuAbove()) notifPermLauncher.launch(getNotificationPermission)
                else storagePermLauncher.launch(getStoragePermission)
            },
            onDismiss = ::dismissDialog
        )

        getErrorResponseContent(showErrorModal)?.let { (title, desc) ->
            HandleDialogError(
                showDialog = true,
                errorTitle = stringResource(title),
                errorDescription = stringResource(desc),
                onClickPrimary = {
                    showErrorModal = null
                    getNetworkStatus(
                        isNetworkAvailable = isNetworkAvailable,
                        onNetworkAvailable = ::getImageAiGenerator,
                        onNetworkUnavailable = { showErrorModal = NETWORK }
                    )
                },
                onClickSecondary = {
                    showErrorModal = null
                    navController.popBackStack()
                }
            )
        }

        AnimatedContent(
            targetState = showShimmer && aiImages.isEmpty(),
            transitionSpec = { (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false)) }
        ) { isLoading ->
            if (isLoading) ImageGeneratorLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp64, start = Dp24, end = Dp24),
                totalImages = args.totalImages
            ) else ImageGeneratorResultContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp64),
                context = context,
                aiImages = aiImages,
                imageSelected = imageSelected.orEmpty(),
                promptText = args.prompt,
                hasCopied = hasCopied,
                scrollState = scrollState,
                snackbarMessage = snackbarMessage,
                onChangeImageSelected = { url -> imageSelected = url },
                onClickCopy = {
                    copyToClipboard(context, args.prompt)
                    hasCopied = true
                },
                onClickDownloadAll = {
                    isDownloadAllClicked = true
                    storagePermLauncher.launch(getStoragePermission)
                },
                onClickReport = { navigateToGmail(context) },
                onClickShare = {
                    scope.launch {
                        val cachePath = File(context.cacheDir, "images")
                        if (!cachePath.exists()) cachePath.mkdirs()
                        val cacheFile = File(cachePath, "shared_image.png")
                        val stream = FileOutputStream(cacheFile)
                        val bitmap = getPhotoThumbnail(imageSelected.orEmpty())
                        bitmap?.compress(
                            /* format = */ PNG,
                            /* quality = */ 100,
                            /* stream = */ stream
                        )
                        stream.close()
                        bitmap?.let {
                            shareContent(
                                context = context,
                                file = cacheFile
                            )
                        }
                        Handler(getMainLooper()).postDelayed({
                            cacheFile.delete()
                        }, 5000)
                    }
                },
                onClickRegenerate = {
                    aiImages = emptyList()
                    getImageAiGenerator()
                },
                onClickDownload = {
                    if (isAndroidTiramisuAbove()) notifPermLauncher.launch(getNotificationPermission)
                    else storagePermLauncher.launch(getStoragePermission)
                }
            )
        }
    }
}

@Composable
private fun ImageGeneratorResultContent(
    context: Context,
    aiImages: List<String>,
    imageSelected: String,
    promptText: String,
    hasCopied: Boolean,
    scrollState: ScrollState,
    snackbarMessage: MutableState<String>,
    modifier: Modifier = Modifier,
    onChangeImageSelected: (String) -> Unit,
    onClickCopy: () -> Unit,
    onClickDownloadAll: () -> Unit,
    onClickReport: () -> Unit,
    onClickShare: () -> Unit,
    onClickRegenerate: () -> Unit,
    onClickDownload: () -> Unit
) = Box(modifier = modifier) {
    Column(
        modifier = Modifier
            .matchParentSize()
            .padding(horizontal = Dp24)
            .navigationBarsPadding()
            .verticalScroll(scrollState),
        verticalArrangement = spacedBy(Dp16)
    ) {
        MeverImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dp12))
                .graphicsLayer {
                    scaleY = 1.1f
                    scaleX = 1.1f
                    clip = true
                },
            source = imageSelected
        )
        if (aiImages.size > 1) Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp8)
        ) {
            aiImages.map { url ->
                MeverImage(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(max = Dp80)
                        .clip(RoundedCornerShape(Dp12))
                        .then(
                            if (imageSelected == url) {
                                Modifier.border(
                                    width = Dp4,
                                    color = colorScheme.primary,
                                    shape = RoundedCornerShape(Dp10)
                                )
                            } else {
                                Modifier.drawWithContent {
                                    drawContent()
                                    drawRect(
                                        color = MeverWhite.copy(alpha = 0.3f),
                                        size = size,
                                        style = Fill
                                    )
                                }
                            }
                        )
                        .onCustomClick { onChangeImageSelected(url) },
                    source = url
                )
            }
        }
        Text(
            text = stringResource(R.string.prompt),
            style = typography.bodyBold1,
            color = colorScheme.onPrimary
        )
        MeverAutoSizableTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp150),
            value = promptText,
            readOnly = true,
            fontSize = Sp18,
            minFontSize = Sp14,
            maxLines = 4
        )
        getMenuActions(context, hasCopied).map { (title, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
                    .background(color = colorScheme.surface, shape = RoundedCornerShape(Dp12))
                    .clip(RoundedCornerShape(Dp12))
                    .onCustomClick {
                        when (icon) {
                            R.drawable.ic_copy -> onClickCopy()
                            R.drawable.ic_download -> onClickDownloadAll()
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
                        tint = colorScheme.onPrimary.copy(alpha = 0.4f),
                        contentDescription = "Copy Prompt"
                    )
                    Text(
                        text = title,
                        style = typography.bodyBold2,
                        color = colorScheme.onPrimary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(Dp120))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(BottomCenter)
    ) {
        Column {
            MeverSnackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp24, vertical = Dp16),
                message = snackbarMessage
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.background)
            ) {
                if (scrollState.canScrollBackward || scrollState.canScrollForward) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = Dp2,
                        color = colorScheme.onPrimary.copy(alpha = 0.12f)
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
                        buttonType = OUTLINED
                    ) { onClickRegenerate() }
                    MeverButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(Dp52),
                        title = stringResource(R.string.download),
                        buttonType = FILLED
                    ) { onClickDownload() }
                }
            }
        }
    }
}

@Composable
private fun ImageGeneratorLoading(
    totalImages: Int,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = spacedBy(Dp16)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(Dp12))
            .background(meverShimmer())
    )
    if (totalImages > 1) Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(Dp8)
    ) {
        repeat(totalImages) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(Dp80)
                    .clip(RoundedCornerShape(Dp12))
                    .background(meverShimmer())
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dp52)
            .clip(RoundedCornerShape(Dp12))
            .background(meverShimmer())
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dp120)
            .clip(RoundedCornerShape(Dp12))
            .background(meverShimmer())
    )
}
