package com.dapascript.mever.feature.ai.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.handleClickButton
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiBackgroundRemovalRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun AiBackgroundRemovalScreen(
    navigator: Navigator,
    viewModel: AiBackgroundRemovalViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val deviceType = LocalDeviceType.current
    val resources = LocalResources.current
    val backgroundRemovalState = backgroundRemovalState.collectAsStateValue()
    val saveImageState = saveImageState.collectAsStateValue()
    val getButtonClickCount = getButtonClickCount.collectAsStateValue()
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var resultBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    val interstitialAd = rememberInterstitialAd {
        resultBitmap?.let {
            saveImage(
                context = context,
                bitmap = it
            )
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            resultBitmap = null
            errorMessage = ""
            reset()
        }
    }

    LaunchedEffect(backgroundRemovalState) {
        when (backgroundRemovalState) {
            is StateLoading -> isProcessing = true
            is StateSuccess -> {
                resultBitmap = backgroundRemovalState.data
                errorMessage = ""
                isProcessing = false
            }

            is StateFailed -> {
                errorMessage = backgroundRemovalState.message.orEmpty()
                isProcessing = false
            }

            else -> Unit
        }
    }

    LaunchedEffect(saveImageState) {
        when (saveImageState) {
            is StateSuccess -> {
                navigator.navigate(
                    route = GalleryLandingRoute,
                    popUpTo = AiBackgroundRemovalRoute,
                    isInclusive = true
                )
            }

            is StateFailed -> Toast.makeText(
                context,
                resources.getString(R.string.failed_save_image),
                LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(title = if (isExpanded.not()) stringResource(R.string.remove_bg) else ""),
        onBackHandler = { navigator.goBack() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64)
        ) {
            if (isExpanded.not() && titleHeight > 0) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(Dp3),
                    thickness = Dp1,
                    color = colors.blackWhite.copy(alpha = 0.12f)
                )
            }
            CompositionLocalProvider(LocalOverscrollFactory provides null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = spacedBy(Dp24),
                    contentPadding = PaddingValues(bottom = Dp40)
                ) {
                    item {
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier.padding(top = Dp16, start = Dp24, end = Dp24),
                                verticalArrangement = spacedBy(Dp8)
                            ) {
                                Text(
                                    modifier = Modifier.onGloballyPositioned {
                                        titleHeight = it.size.height
                                    },
                                    text = stringResource(R.string.remove_bg),
                                    style = typography.h2.copy(fontSize = Sp32),
                                    color = colors.blackWhite
                                )
                                Text(
                                    text = stringResource(R.string.remove_bg_desc),
                                    style = typography.body2,
                                    color = colors.grayLightGray
                                )
                            }
                        }
                    }
                    item {
                        if (deviceType != DeviceType.PHONE) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Dp24),
                                horizontalArrangement = spacedBy(Dp24),
                                verticalAlignment = CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1.2f)) {
                                    ImagePreviewCard(
                                        imageUri = imageUri,
                                        resultBitmap = resultBitmap,
                                        isProcessing = isProcessing,
                                        onPickImage = {
                                            if (imageUri == null) imagePicker.launch("image/*")
                                        }
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = spacedBy(Dp20)
                                ) {
                                    ActionButtons(
                                        imageUri = imageUri,
                                        resultBitmap = resultBitmap,
                                        isProcessing = isProcessing,
                                        isSaving = saveImageState is StateLoading,
                                        onPickImage = { imagePicker.launch("image/*") },
                                        onRemoveBackground = {
                                            imageUri?.let {
                                                removeBackground(
                                                    context = context,
                                                    imageUri = it
                                                )
                                            }
                                        },
                                        onSaveImage = {
                                            handleClickButton(
                                                buttonClickCount = getButtonClickCount,
                                                onIncrementClickCount = { incrementClickCount() },
                                                onShowAds = { interstitialAd.showAd() },
                                                onClickAction = {
                                                    resultBitmap?.let {
                                                        saveImage(
                                                            context = context,
                                                            bitmap = it
                                                        )
                                                    }
                                                }
                                            )
                                        },
                                        onClearImage = {
                                            imageUri = null
                                            resultBitmap = null
                                            errorMessage = ""
                                            reset()
                                        }
                                    )
                                    ProcessingHint(
                                        imageUri = imageUri,
                                        resultBitmap = resultBitmap,
                                        isProcessing = isProcessing,
                                        errorMessage = errorMessage
                                    )
                                }
                            }
                        } else {
                            ActionPanel(
                                modifier = Modifier.padding(horizontal = Dp24),
                                imageUri = imageUri,
                                resultBitmap = resultBitmap,
                                isProcessing = isProcessing,
                                isSaving = saveImageState is StateLoading,
                                errorMessage = errorMessage,
                                onPickImage = { imagePicker.launch("image/*") },
                                onRemoveBackground = {
                                    imageUri?.let {
                                        removeBackground(
                                            context = context,
                                            imageUri = it
                                        )
                                    }
                                },
                                onSaveImage = {
                                    resultBitmap?.let { saveImage(context = context, bitmap = it) }
                                },
                                onClearImage = {
                                    imageUri = null
                                    resultBitmap = null
                                    errorMessage = ""
                                    reset()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionPanel(
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    isProcessing: Boolean,
    isSaving: Boolean,
    errorMessage: String,
    onPickImage: () -> Unit,
    onRemoveBackground: () -> Unit,
    onSaveImage: () -> Unit,
    onClearImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(Dp32))
            .background(colors.whiteDarkGray)
            .border(Dp1, colors.blackWhite.copy(alpha = 0.08f), RoundedCornerShape(Dp32))
            .padding(Dp16),
        verticalArrangement = spacedBy(Dp20)
    ) {
        ImagePreviewCard(
            imageUri = imageUri,
            resultBitmap = resultBitmap,
            isProcessing = isProcessing,
            onPickImage = onPickImage
        )

        ActionButtons(
            imageUri = imageUri,
            resultBitmap = resultBitmap,
            isProcessing = isProcessing,
            isSaving = isSaving,
            onPickImage = onPickImage,
            onRemoveBackground = onRemoveBackground,
            onSaveImage = onSaveImage,
            onClearImage = onClearImage
        )

        ProcessingHint(
            imageUri = imageUri,
            resultBitmap = resultBitmap,
            isProcessing = isProcessing,
            errorMessage = errorMessage
        )
    }
}

@Composable
private fun ActionButtons(
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    isProcessing: Boolean,
    isSaving: Boolean,
    onPickImage: () -> Unit,
    onRemoveBackground: () -> Unit,
    onSaveImage: () -> Unit,
    onClearImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(Dp12)
    ) {
        MeverButton(
            modifier = Modifier
                .weight(1f)
                .height(Dp52),
            title = when {
                imageUri == null -> stringResource(R.string.select_image)
                resultBitmap != null -> stringResource(R.string.save_png)
                else -> stringResource(R.string.process_now)
            },
            buttonType = Filled(
                backgroundColor = colors.alwaysPurple,
                contentColor = MeverWhite
            ),
            isLoading = isProcessing || isSaving,
            isEnabled = !isProcessing && !isSaving,
            onClick = when {
                imageUri == null -> onPickImage
                resultBitmap != null -> onSaveImage
                else -> onRemoveBackground
            }
        )
        if (imageUri != null) {
            MeverButton(
                modifier = Modifier
                    .weight(0.4f)
                    .height(Dp52),
                title = stringResource(R.string.clear),
                buttonType = Outlined(
                    contentColor = colors.blackWhite,
                    borderColor = colors.blackWhite.copy(alpha = 0.15f)
                ),
                isEnabled = !isProcessing && !isSaving,
                onClick = onClearImage
            )
        }
    }
}

@Composable
private fun ImagePreviewCard(
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    isProcessing: Boolean,
    onPickImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(28.dp))
            .background(colors.whiteDark)
            .drawBehind {
                val sizePx = 20.dp.toPx()
                val columns = (size.width / sizePx).toInt() + 1
                val rows = (size.height / sizePx).toInt() + 1
                for (i in 0 until columns) {
                    for (j in 0 until rows) {
                        if ((i + j) % 2 == 0) {
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.1f),
                                topLeft = Offset(i * sizePx, j * sizePx),
                                size = Size(sizePx, sizePx)
                            )
                        }
                    }
                }
            }
            .border(Dp1, colors.blackWhite.copy(alpha = 0.08f), RoundedCornerShape(28.dp))
            .onCustomClick(enabled = isProcessing.not()) { onPickImage() },
        contentAlignment = Center
    ) {
        if (imageUri == null) EmptyPickerState()
        else {
            if (resultBitmap != null) {
                Image(
                    bitmap = resultBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                MeverImage(
                    source = imageUri,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(28.dp))
                )
            }
        }

        if (isProcessing) ProcessingOverlay()
    }
}

@Composable
private fun EmptyPickerState() {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(Dp12)
    ) {
        Box(
            modifier = Modifier
                .size(Dp64)
                .clip(CircleShape)
                .background(colors.alwaysPurple.copy(alpha = 0.1f)),
            contentAlignment = Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_explore_image),
                contentDescription = null,
                tint = colors.alwaysPurple,
                modifier = Modifier.size(Dp32)
            )
        }
        Text(
            text = stringResource(R.string.pick_portrait),
            style = typography.bodyBold2,
            color = colors.blackWhite
        )
        Text(
            text = stringResource(R.string.supported_format),
            style = typography.body3,
            color = colors.grayLightGray
        )
    }
}

@Composable
private fun ProcessingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.whiteDark.copy(alpha = 0.7f)),
        contentAlignment = Center
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(Dp12)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dp40),
                color = colors.alwaysPurple,
                strokeCap = Round
            )
            Text(
                text = stringResource(R.string.processing_ai),
                style = typography.bodyBold3,
                color = colors.blackWhite
            )
        }
    }
}

@Composable
private fun ProcessingHint(
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    isProcessing: Boolean,
    errorMessage: String
) {
    val (text, color) = when {
        errorMessage.isNotEmpty() -> errorMessage to colors.alwaysPurple
        isProcessing -> stringResource(R.string.hint_generating) to colors.grayLightGray
        imageUri == null -> stringResource(R.string.hint_mlkit) to colors.grayLightGray
        resultBitmap == null -> stringResource(R.string.hint_ready) to colors.grayLightGray
        else -> stringResource(R.string.hint_success) to colors.alwaysPurple
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dp16))
            .background(colors.whiteDark)
            .border(Dp1, colors.blackWhite.copy(alpha = 0.05f), RoundedCornerShape(Dp16))
            .padding(Dp16),
        horizontalArrangement = spacedBy(Dp12),
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dp10)
                .clip(CircleShape)
                .background(if (resultBitmap != null) colors.alwaysPurple else color.copy(alpha = 0.4f))
        )
        Text(
            text = text,
            style = typography.body3,
            color = color,
            modifier = Modifier.weight(1f)
        )
    }
}
