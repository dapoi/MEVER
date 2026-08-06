package com.dapascript.mever.feature.ai.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.BLUE
import android.graphics.Color.DKGRAY
import android.graphics.Color.MAGENTA
import android.graphics.Color.RED
import android.graphics.Color.WHITE
import android.net.Uri
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverSnackbar
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.core.common.util.navigateToSystemGallery
import com.dapascript.mever.core.common.util.onClickWithAds
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.syncToGallery
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiBackgroundRemovalRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel.BgRemovalBackground
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel.ImageLocation.GALLERY
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel.ImageLocation.IN_APP

@OptIn(ExperimentalMaterial3Api::class)
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
    val selectedBackground = selectedBackground.collectAsStateValue()
    val getButtonClickCount = getButtonClickCount.collectAsStateValue()
    val adsThreshold = adsThreshold.collectAsStateValue()
    var isLoading by remember { mutableStateOf(false) }
    var isSaved by rememberSaveable { mutableStateOf(false) }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var resultBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    var showColorPicker by remember { mutableStateOf(false) }
    var hue by rememberSaveable { mutableFloatStateOf(0f) }
    var saturation by rememberSaveable { mutableFloatStateOf(1f) }
    var value by rememberSaveable { mutableFloatStateOf(1f) }
    val color = remember(hue, saturation, value) { Color.hsv(hue, saturation, value) }

    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    val interstitialAd = rememberInterstitialAd {
        resultBitmap?.let {
            saveImage(bitmap = it)
        }
    }
    val imagePicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
            resultBitmap = null
            errorMessage = ""
            isSaved = false
            reset()
        }
    }
    val backgroundPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            loadBackgroundBitmap(uri)
        }
    }

    LaunchedEffect(backgroundRemovalState) {
        backgroundRemovalState.handleUiState(
            onLoading = { isProcessing = true },
            onSuccess = { bitmap ->
                resultBitmap = bitmap
                errorMessage = ""
                isProcessing = false
            },
            onFailed = { message ->
                errorMessage = message.orEmpty()
                isProcessing = false
            }
        )
    }

    LaunchedEffect(saveImageState) {
        saveImageState.handleUiState(
            onLoading = { isLoading = true },
            onSuccess = { result ->
                isLoading = false
                val location = result.location
                val fileName = result.fileName

                when (location) {
                    IN_APP -> {
                        isSaved = true
                        navigator.navigate(
                            route = GalleryLandingRoute,
                            popUpTo = AiBackgroundRemovalRoute,
                            isInclusive = true
                        )
                    }

                    GALLERY -> {
                        if (isSaved.not()) {
                            snackbarMessage.value = resources.getString(
                                R.string.success_save_image
                            )
                        }
                        isSaved = true
                    }
                }

                syncToGallery(context, fileName)
            },
            onFailed = { message ->
                isLoading = false
                Toast.makeText(
                    context,
                    message ?: resources.getString(R.string.failed_save_image),
                    LENGTH_SHORT
                ).show()
            }
        )
    }

    MeverDialog(
        showDialog = showColorPicker,
        title = stringResource(R.string.choose_color),
        description = null,
        image = null,
        primaryActionLabel = stringResource(R.string.apply),
        secondaryActionLabel = stringResource(R.string.cancel),
        onClickSecondaryAction = { showColorPicker = false },
        onClickPrimaryAction = {
            selectBackground(BgRemovalBackground.Color(color.toArgb()))
            showColorPicker = false
        },
        content = {
            Column(
                modifier = Modifier.padding(vertical = Dp16),
                verticalArrangement = spacedBy(Dp20),
                horizontalAlignment = CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = spacedBy(Dp8)
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dp64)
                            .clip(CircleShape)
                            .background(color)
                            .border(Dp1, colors.blackWhite.copy(alpha = 0.1f), CircleShape)
                    )
                    Surface(
                        onClick = {
                            val hex = String.format("#%06X", (0xFFFFFF and color.toArgb()))
                            copyToClipboard(context, hex)
                            Toast.makeText(
                                context,
                                resources.getString(R.string.copied),
                                LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(Dp12),
                        color = colors.blackWhite.copy(alpha = 0.05f)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = Dp12, vertical = Dp4),
                            text = String.format("#%06X", (0xFFFFFF and color.toArgb())),
                            style = typography.bodyBold3.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            ),
                            color = colors.blackWhite
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dp16),
                    verticalArrangement = spacedBy(Dp16)
                ) {
                    Column(verticalArrangement = spacedBy(Dp4)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.hue),
                                style = typography.bodyBold3,
                                color = colors.blackWhite
                            )
                            Text(
                                text = "${hue.toInt()}°",
                                style = typography.body3,
                                color = colors.grayLightGray
                            )
                        }
                        Slider(
                            modifier = Modifier.height(24.dp),
                            value = hue,
                            onValueChange = { hue = it },
                            valueRange = 0f..360f,
                            colors = SliderDefaults.colors(
                                thumbColor = colors.alwaysPurple,
                                activeTrackColor = MeverTransparent,
                                inactiveTrackColor = MeverTransparent
                            ),
                            track = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Red, Yellow, Green,
                                                    Cyan, Blue, Magenta, Red
                                                )
                                            )
                                        )
                                )
                            }
                        )
                    }
                    Column(verticalArrangement = spacedBy(Dp4)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.saturation),
                                style = typography.bodyBold3,
                                color = colors.blackWhite
                            )
                            Text(
                                text = "${(saturation * 100).toInt()}%",
                                style = typography.body3,
                                color = colors.grayLightGray
                            )
                        }
                        Slider(
                            modifier = Modifier.height(24.dp),
                            value = saturation,
                            onValueChange = { saturation = it },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = colors.alwaysPurple,
                                activeTrackColor = MeverTransparent,
                                inactiveTrackColor = MeverTransparent
                            ),
                            track = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.White,
                                                    Color.hsv(hue, 1f, 1f)
                                                )
                                            )
                                        )
                                )
                            }
                        )
                    }
                    Column(verticalArrangement = spacedBy(Dp4)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.value),
                                style = typography.bodyBold3,
                                color = colors.blackWhite
                            )
                            Text(
                                text = "${(value * 100).toInt()}%",
                                style = typography.body3,
                                color = colors.grayLightGray
                            )
                        }
                        Slider(
                            modifier = Modifier.height(24.dp),
                            value = value,
                            onValueChange = { value = it },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = colors.alwaysPurple,
                                activeTrackColor = MeverTransparent,
                                inactiveTrackColor = MeverTransparent
                            ),
                            track = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.Black,
                                                    Color.hsv(hue, saturation, 1f)
                                                )
                                            )
                                        )
                                )
                            }
                        )
                    }
                }
            }
        }
    )

    BaseScreen(
        topBarArgs = TopBarArgs(title = if (isExpanded.not()) stringResource(R.string.remove_bg) else ""),
        onBackHandler = { navigator.goBack() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                                    modifier = Modifier.padding(
                                        top = Dp16,
                                        start = Dp24,
                                        end = Dp24
                                    ),
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
                            if (deviceType == PHONE) {
                                ActionPanel(
                                    modifier = Modifier
                                        .padding(horizontal = Dp24)
                                        .clip(RoundedCornerShape(Dp32))
                                        .background(colors.whiteDarkGray)
                                        .border(
                                            width = Dp1,
                                            color = colors.blackWhite.copy(alpha = 0.08f),
                                            shape = RoundedCornerShape(Dp32)
                                        )
                                        .padding(vertical = Dp16),
                                    imageUri = imageUri,
                                    resultBitmap = resultBitmap,
                                    selectedBackground = selectedBackground,
                                    isProcessing = isProcessing,
                                    isLoading = isLoading,
                                    isSaved = isSaved,
                                    errorMessage = errorMessage,
                                    onPickImage = {
                                        if (imageUri == null) imagePicker.launch(
                                            PickVisualMediaRequest(ImageOnly)
                                        )
                                    },
                                    onPickBackground = {
                                        backgroundPicker.launch(PickVisualMediaRequest(ImageOnly))
                                    },
                                    onSelectBackground = { selectBackground(it) },
                                    onOpenColorPicker = { showColorPicker = true },
                                    onPreviewImage = {
                                        resultBitmap?.let { bitmap ->
                                            saveToCache(bitmap) { path ->
                                                path?.let {
                                                    navigator.navigate(
                                                        GalleryContentDetailRoute(
                                                            contents = listOf(
                                                                Content(
                                                                    id = 0,
                                                                    isVideo = false,
                                                                    fileName = resources.getString(R.string.preview),
                                                                    media = path,
                                                                    isPreview = true,
                                                                    isDeletable = false,
                                                                    isDownloadable = false
                                                                )
                                                            ),
                                                            initialIndex = 0
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    onRemoveBackground = {
                                        imageUri?.let { removeBackground(imageUri = it) }
                                    },
                                    onSaveImage = {
                                        onClickWithAds(
                                            buttonClickCount = getButtonClickCount,
                                            adsThreshold = adsThreshold,
                                            onIncrementClickCount = { incrementClickCount() },
                                            onShowAds = { interstitialAd.showAd() },
                                            onClickAction = {
                                                resultBitmap?.let { saveImage(bitmap = it) }
                                            }
                                        )
                                    },
                                    onOpenGallery = { navigateToSystemGallery(context) },
                                    onClearImage = {
                                        imageUri = null
                                        resultBitmap = null
                                        errorMessage = ""
                                        isSaved = false
                                        reset()
                                    }
                                )
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dp24),
                                    horizontalArrangement = spacedBy(Dp24),
                                    verticalAlignment = CenterVertically
                                ) {
                                    Box(modifier = Modifier.weight(1.2f)) {
                                        ImagePreviewCard(
                                            modifier = Modifier.fillMaxWidth(),
                                            isProcessing = isProcessing,
                                            imageUri = imageUri,
                                            resultBitmap = resultBitmap,
                                            selectedBackground = selectedBackground,
                                            onPickImage = {
                                                if (imageUri == null) imagePicker.launch(
                                                    PickVisualMediaRequest(ImageOnly)
                                                )
                                            },
                                            onPreviewImage = {
                                                resultBitmap?.let { bitmap ->
                                                    saveToCache(bitmap) { path ->
                                                        path?.let {
                                                            navigator.navigate(
                                                                GalleryContentDetailRoute(
                                                                    contents = listOf(
                                                                        Content(
                                                                            id = 0,
                                                                            isVideo = false,
                                                                            fileName = resources.getString(
                                                                                R.string.preview
                                                                            ),
                                                                            media = path,
                                                                            isPreview = true,
                                                                            isDeletable = false,
                                                                            isDownloadable = false
                                                                        )
                                                                    ),
                                                                    initialIndex = 0
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = spacedBy(Dp20)
                                    ) {
                                        if (resultBitmap != null) {
                                            BackgroundSelection(
                                                modifier = Modifier.fillMaxWidth(),
                                                selectedBackground = selectedBackground,
                                                onPickBackground = {
                                                    backgroundPicker.launch(
                                                        PickVisualMediaRequest(ImageOnly)
                                                    )
                                                },
                                                onSelectBackground = { selectBackground(it) },
                                                onOpenColorPicker = { showColorPicker = true }
                                            )
                                        }
                                        ActionButtons(
                                            modifier = Modifier.fillMaxWidth(),
                                            isProcessing = isProcessing,
                                            isLoading = isLoading,
                                            isSaved = isSaved,
                                            imageUri = imageUri,
                                            resultBitmap = resultBitmap,
                                            onPickImage = {
                                                if (imageUri == null) imagePicker.launch(
                                                    PickVisualMediaRequest(ImageOnly)
                                                )
                                            },
                                            onRemoveBackground = {
                                                imageUri?.let { removeBackground(imageUri = it) }
                                            },
                                            onSaveImage = {
                                                onClickWithAds(
                                                    buttonClickCount = getButtonClickCount,
                                                    adsThreshold = adsThreshold,
                                                    onIncrementClickCount = { incrementClickCount() },
                                                    onShowAds = { interstitialAd.showAd() },
                                                    onClickAction = {
                                                        resultBitmap?.let { saveImage(bitmap = it) }
                                                    }
                                                )
                                            },
                                            onOpenGallery = { navigateToSystemGallery(context) },
                                            onClearImage = {
                                                imageUri = null
                                                resultBitmap = null
                                                errorMessage = ""
                                                isSaved = false
                                                reset()
                                            }
                                        )
                                        ProcessingHint(
                                            modifier = Modifier.fillMaxWidth(),
                                            isProcessing = isProcessing,
                                            errorMessage = errorMessage,
                                            imageUri = imageUri,
                                            resultBitmap = resultBitmap
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            MeverSnackbar(
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(start = Dp24, end = Dp24, bottom = Dp40),
                message = snackbarMessage,
                duration = Long,
                actionMessage = stringResource(R.string.view),
                onClickSnackbarAction = {
                    snackbarMessage.value = ""
                    navigateToSystemGallery(context)
                }
            )
        }
    }
}

@Composable
private fun ActionPanel(
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    selectedBackground: BgRemovalBackground,
    isProcessing: Boolean,
    isLoading: Boolean,
    isSaved: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onPickImage: () -> Unit,
    onPickBackground: () -> Unit,
    onSelectBackground: (BgRemovalBackground) -> Unit,
    onOpenColorPicker: () -> Unit,
    onPreviewImage: () -> Unit,
    onRemoveBackground: () -> Unit,
    onSaveImage: () -> Unit,
    onOpenGallery: () -> Unit,
    onClearImage: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp20)
    ) {
        ImagePreviewCard(
            modifier = Modifier.padding(horizontal = Dp16),
            isProcessing = isProcessing,
            imageUri = imageUri,
            resultBitmap = resultBitmap,
            selectedBackground = selectedBackground,
            onPickImage = onPickImage,
            onPreviewImage = onPreviewImage
        )
        if (resultBitmap != null) {
            BackgroundSelection(
                modifier = Modifier.fillMaxWidth(),
                selectedBackground = selectedBackground,
                onPickBackground = onPickBackground,
                onSelectBackground = onSelectBackground,
                onOpenColorPicker = onOpenColorPicker
            )
        }
        ActionButtons(
            modifier = Modifier.padding(horizontal = Dp16),
            isProcessing = isProcessing,
            isLoading = isLoading,
            isSaved = isSaved,
            imageUri = imageUri,
            resultBitmap = resultBitmap,
            onPickImage = onPickImage,
            onRemoveBackground = onRemoveBackground,
            onSaveImage = onSaveImage,
            onOpenGallery = onOpenGallery,
            onClearImage = onClearImage
        )
        ProcessingHint(
            modifier = Modifier.padding(horizontal = Dp16),
            isProcessing = isProcessing,
            errorMessage = errorMessage,
            imageUri = imageUri,
            resultBitmap = resultBitmap
        )
    }
}

@Composable
private fun ActionButtons(
    isProcessing: Boolean,
    isLoading: Boolean,
    isSaved: Boolean,
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    onPickImage: () -> Unit,
    onRemoveBackground: () -> Unit,
    onSaveImage: () -> Unit,
    onOpenGallery: () -> Unit,
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
                isSaved -> stringResource(R.string.view_in_gallery)
                resultBitmap != null -> stringResource(R.string.save_png)
                else -> stringResource(R.string.process_now)
            },
            buttonType = Filled(
                backgroundColor = colors.alwaysPurple,
                contentColor = MeverWhite
            ),
            isLoading = isProcessing || isLoading,
            isEnabled = isProcessing.not() && isLoading.not(),
            onClick = when {
                imageUri == null -> onPickImage
                isSaved -> onOpenGallery
                resultBitmap != null -> onSaveImage
                else -> onRemoveBackground
            }
        )
        if (imageUri != null && isLoading.not()) {
            MeverButton(
                modifier = Modifier
                    .weight(0.4f)
                    .height(Dp52),
                title = stringResource(R.string.clear),
                buttonType = Outlined(
                    borderColor = colors.alwaysPurple,
                    contentColor = colors.alwaysPurple
                ),
                onClick = onClearImage
            )
        }
    }
}

@Composable
private fun ImagePreviewCard(
    isProcessing: Boolean,
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    selectedBackground: BgRemovalBackground,
    onPickImage: () -> Unit,
    onPreviewImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(28.dp))
            .background(
                if (selectedBackground is BgRemovalBackground.Color) Color(selectedBackground.color)
                else colors.whiteDark
            )
            .drawBehind {
                if (selectedBackground is BgRemovalBackground.Transparent) {
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
            }
            .border(Dp1, colors.blackWhite.copy(alpha = 0.08f), RoundedCornerShape(28.dp))
            .onCustomClick(enabled = isProcessing.not()) {
                if (resultBitmap != null) onPreviewImage()
                else onPickImage()
            },
        contentAlignment = Center
    ) {
        if (selectedBackground is BgRemovalBackground.Image) {
            MeverImage(
                source = selectedBackground.uri,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(28.dp))
            )
        }
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
    isProcessing: Boolean,
    errorMessage: String,
    imageUri: Uri?,
    resultBitmap: Bitmap?,
    modifier: Modifier = Modifier
) {
    val (text, color) = when {
        errorMessage.isNotEmpty() -> errorMessage to colors.alwaysPurple
        isProcessing -> stringResource(R.string.hint_generating) to colors.grayLightGray
        imageUri == null -> stringResource(R.string.hint_mlkit) to colors.grayLightGray
        resultBitmap == null -> stringResource(R.string.hint_ready) to colors.grayLightGray
        else -> stringResource(R.string.hint_success) to colors.alwaysPurple
    }

    Row(
        modifier = modifier
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

@Composable
private fun BackgroundSelection(
    selectedBackground: BgRemovalBackground,
    modifier: Modifier = Modifier,
    onPickBackground: () -> Unit,
    onSelectBackground: (BgRemovalBackground) -> Unit,
    onOpenColorPicker: () -> Unit
) {
    val backgroundColors = listOf(WHITE, BLACK, RED, BLUE, MAGENTA, DKGRAY)

    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp12)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = Dp16),
            text = stringResource(R.string.change_bg),
            style = typography.bodyBold3,
            color = colors.blackWhite
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp12),
            contentPadding = PaddingValues(horizontal = Dp16)
        ) {
            item {
                Surface(
                    modifier = Modifier.size(Dp40),
                    shape = CircleShape,
                    color = colors.whiteDark,
                    border = border(selectedBackground is BgRemovalBackground.Transparent),
                    onClick = { onSelectBackground(BgRemovalBackground.Transparent) }
                ) {
                    Box(contentAlignment = Center) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                            contentDescription = null,
                            tint = colors.blackWhite,
                            modifier = Modifier.size(Dp20)
                        )
                    }
                }
            }
            item {
                Surface(
                    modifier = Modifier.size(Dp40),
                    shape = CircleShape,
                    color = colors.whiteDark,
                    border = border(selectedBackground is BgRemovalBackground.Image),
                    onClick = onPickBackground
                ) {
                    Box(contentAlignment = Center) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_explore_image),
                            contentDescription = null,
                            tint = colors.alwaysPurple,
                            modifier = Modifier.size(Dp20)
                        )
                    }
                }
            }
            item {
                Surface(
                    modifier = Modifier.size(Dp40),
                    shape = CircleShape,
                    color = colors.whiteDark,
                    border = border(false),
                    onClick = onOpenColorPicker
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.sweepGradient(
                                    colors = listOf(Red, Magenta, Blue, Cyan, Green, Yellow, Red)
                                )
                            ),
                        contentAlignment = Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_copy),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(Dp20)
                        )
                    }
                }
            }
            items(backgroundColors) { color ->
                Surface(
                    modifier = Modifier.size(Dp40),
                    shape = CircleShape,
                    color = Color(color),
                    border = border(
                        selectedBackground is BgRemovalBackground.Color && selectedBackground.color == color
                    ),
                    onClick = { onSelectBackground(BgRemovalBackground.Color(color)) }
                ) {}
            }
        }
    }
}

@Composable
private fun border(isSelected: Boolean) = if (isSelected) {
    BorderStroke(2.dp, colors.alwaysPurple)
} else {
    BorderStroke(1.dp, colors.blackWhite.copy(alpha = 0.1f))
}