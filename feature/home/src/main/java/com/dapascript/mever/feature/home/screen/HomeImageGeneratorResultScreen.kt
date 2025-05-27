package com.dapascript.mever.feature.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.util.getNetworkStatus
import com.dapascript.mever.core.data.model.local.ImageAiEntity
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
    var showErrorNetworkModal by remember { mutableStateOf(false) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            screenName = "Image Generator",
            onClickBack = { navController.popBackStack() }
        )
    ) {
        aiImages?.let {
            ImageGeneratorResultContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dp12)),
                imageAiEntity = it,
                totalImages = args.totalImages,
                onClickImage = {
                },
                onClickDownload = {

                }
            )
        } ?: AnimatedVisibility(true) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(Dp12))
                    .background(meverShimmer())
            )
        }

        LaunchedEffect(isNetworkAvailable.value) {
            getNetworkStatus(
                isNetworkAvailable = isNetworkAvailable.value,
                onNetworkAvailable = ::getImageAiGenerator,
                onNetworkUnavailable = { showErrorNetworkModal = true }
            )
        }

        LaunchedEffect(aiResponseState) {
            aiResponseState.handleUiState(
                onLoading = { showShimmer = true },
                onSuccess = { result ->
                    showShimmer = false
                    aiImages = result
                },
                onFailed = {
                    showShimmer = false
                }
            )
        }
    }
}

@Composable
private fun ImageGeneratorResultContent(
    totalImages: Int,
    imageAiEntity: ImageAiEntity,
    modifier: Modifier = Modifier,
    onClickImage: (String) -> Unit,
    onClickDownload: (String) -> Unit
) = with(imageAiEntity) {
//    when (totalImages) {
//        1 -> RenderImageAi(
//            image = imagesUrl.first(),
//            modifier = modifier.aspectRatio(1f)
//        )
//
//        2 -> Row(
//            modifier = modifier.height(Dp200),
//            horizontalArrangement = SpaceBetween
//        ) {
//            imagesUrl.take(2).forEach { url ->
//                RenderImageAi(
//                    image = url,
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight()
//                        .clip(RoundedCornerShape(Dp8))
//                )
//            }
//        }
//        3 -> Column(
//            modifier = modifier.aspectRatio(1f),
//            verticalArrangement = Arrangement.spacedBy(Dp8)
//        ) {
//            RenderImageAi(
//                image = imagesUrl[0],
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .clip(RoundedCornerShape(Dp8)),
//            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                horizontalArrangement = Arrangement.spacedBy(Dp8)
//            ) {
//                imagesUrl.drop(1).forEach { url ->
//                    RenderImageAi(
//                        image = url,
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .clip(RoundedCornerShape(Dp8))
//                    )
//                }
//            }
//        }
//
//        4 -> Column(
//            modifier = modifier.aspectRatio(1f),
//            verticalArrangement = Arrangement.spacedBy(Dp8)
//        ) {
//            imagesUrl.chunked(2).forEach { rowImages ->
//                Row(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(Dp8)
//                ) {
//                    rowImages.forEach { url ->
//                        RenderImageAi(
//                            image = url,
//                            modifier = Modifier
//                                .weight(1f)
//                                .fillMaxHeight()
//                                .clip(RoundedCornerShape(Dp8)),
//                        )
//                    }
//                }
//            }
//        }
//    }
}

@Composable
private fun RenderImageAi(
    image: String,
    modifier: Modifier = Modifier
) = SubcomposeAsyncImage(
    modifier = modifier,
    model = ImageRequest.Builder(LocalContext.current)
        .data(image)
        .crossfade(true)
        .build(),
    contentScale = Crop,
    contentDescription = "Image"
)