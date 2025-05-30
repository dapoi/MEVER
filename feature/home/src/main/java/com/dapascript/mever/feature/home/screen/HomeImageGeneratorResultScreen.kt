package com.dapascript.mever.feature.home.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.NETWORK
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.RESPONSE
import com.dapascript.mever.core.common.util.ErrorHandle.getErrorResponseContent
import com.dapascript.mever.core.common.util.getNetworkStatus
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.feature.home.screen.component.HandleDialogError
import com.dapascript.mever.feature.home.viewmodel.HomeImageGeneratorResultViewModel

@Composable
internal fun HomeImageGeneratorResultScreen(
    navController: NavController,
    viewModel: HomeImageGeneratorResultViewModel = hiltViewModel()
) = with(viewModel) {
    val aiResponseState = aiResponseState.collectAsStateValue()
    val isNetworkAvailable = connectivityObserver.observe().collectAsState(
        connectivityObserver.isConnected()
    )
    var aiImages by remember { mutableStateOf<ImageAiEntity?>(null) }
    var showShimmer by remember { mutableStateOf(false) }
    var showErrorModal by remember { mutableStateOf<ErrorType?>(null) }
    var showCancelExitConfirmation by remember { mutableStateOf(false) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = "Image Generator",
            onClickBack = { showCancelExitConfirmation = true }
        )
    ) {
        LaunchedEffect(isNetworkAvailable.value) {
            getNetworkStatus(
                isNetworkAvailable = isNetworkAvailable.value,
                onNetworkAvailable = ::getImageAiGenerator,
                onNetworkUnavailable = { showErrorModal = NETWORK }
            )
        }

        LaunchedEffect(aiResponseState) {
            aiResponseState.handleUiState(
                onLoading = { showShimmer = true },
                onSuccess = { result -> aiImages = result },
                onFailed = { showErrorModal = RESPONSE }
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

        getErrorResponseContent(showErrorModal)?.let { (title, desc) ->
            HandleDialogError(
                showDialog = true,
                errorTitle = stringResource(title),
                errorDescription = stringResource(desc),
                onRetry = {
                    showErrorModal = null
                    getNetworkStatus(
                        isNetworkAvailable = isNetworkAvailable.value,
                        onNetworkAvailable = ::getImageAiGenerator,
                        onNetworkUnavailable = { showErrorModal = NETWORK }
                    )
                },
                onDismiss = {
                    showErrorModal = null
                    navController.popBackStack()
                }
            )
        }

        if (showShimmer && aiImages == null) ImageGeneratorLoading(
            modifier = Modifier.fillMaxWidth(),
            totalImages = args.totalImages
        )

        aiImages?.let { images ->
            ImageGeneratorResultContent(
                modifier = Modifier.fillMaxWidth(),
                imageAiEntity = images,
                totalImages = args.totalImages,
                promptText = args.prompt,
                onClickDownload = {}
            )
        }
    }
}

@Composable
private fun ImageGeneratorResultContent(
    totalImages: Int,
    promptText: String,
    imageAiEntity: ImageAiEntity,
    modifier: Modifier = Modifier,
    onClickDownload: (String) -> Unit
) = with(imageAiEntity) {
    var imageSelected by remember(imagesUrl) { mutableStateOf(imagesUrl.firstOrNull()) }
    var hasCopied by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = spacedBy(Dp16)
    ) {
        MeverImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dp12)),
            source = imageSelected
        )
        if (totalImages > 1) Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp8)
        ) {
            imagesUrl.take(totalImages).map { url ->
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
                        .onCustomClick { imageSelected = url },
                    source = url
                )
            }
        }
        Text(
            text = stringResource(R.string.prompt),
            style = typography.bodyBold1,
            color = colorScheme.onPrimary
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp120)
                .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
                .background(color = colorScheme.surface, shape = RoundedCornerShape(Dp12))
                .clip(RoundedCornerShape(Dp12))
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(Dp12),
                text = promptText,
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
                .background(color = colorScheme.surface, shape = RoundedCornerShape(Dp12))
                .clip(RoundedCornerShape(Dp12))
                .onCustomClick {
                    clipboardManager.setText(AnnotatedString(promptText))
                    hasCopied = true
                }
        ) {
            Row(
                modifier = Modifier.padding(Dp10),
                horizontalArrangement = spacedBy(Dp8),
                verticalAlignment = CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(Dp20),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_copy),
                    tint = colorScheme.onPrimary.copy(alpha = 0.4f),
                    contentDescription = "Copy Prompt"
                )
                Text(
                    text = stringResource(if (hasCopied) R.string.copied else R.string.copy),
                    style = typography.bodyBold2,
                    color = colorScheme.onPrimary.copy(alpha = 0.4f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp8),
            verticalAlignment = CenterVertically
        ) {
            MeverButton(
                modifier = Modifier
                    .weight(1f)
                    .height(Dp52),
                title = stringResource(R.string.regenerate),
                buttonType = OUTLINED
            ) { }
            MeverButton(
                modifier = Modifier
                    .weight(1f)
                    .height(Dp52),
                title = stringResource(R.string.download),
                buttonType = FILLED
            ) { }
        }
    }
}

@Composable
private fun ImageGeneratorLoading(
    totalImages: Int,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = spacedBy(Dp8)
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
}