package com.dapascript.mever.feature.explore.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp160
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp250
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.DpHalf
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp10
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.FadeSide.Bottom
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.fadingEdge
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.data.model.local.ArtEntity
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.explore.viewmodel.ArtImageViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.lang.System.currentTimeMillis
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
@Composable
internal fun ArtImageScreen(
    navigator: Navigator,
    viewModel: ArtImageViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val deviceType = LocalDeviceType.current
    val artResponseState = artResponseState.collectAsStateValue()
    val gridState = rememberLazyStaggeredGridState()
    var errorMessage by remember { mutableStateOf("") }
    var lastQuery by rememberSaveable { mutableStateOf("") }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    var lastExpandedState by rememberSaveable { mutableStateOf(true) }
    var selectedArtwork by remember { mutableStateOf<ArtEntity?>(null) }

    val isExpanded = remember(gridState, titleHeight, artworks, lastExpandedState) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            if (artworks == null) return@derivedStateOf lastExpandedState

            gridState.firstVisibleItemIndex == 0 &&
                    gridState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    val showBottomFade by remember {
        derivedStateOf { gridState.canScrollForward }
    }
    val expanded by isExpanded

    val shouldLoadMore = remember(gridState) {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= totalItemsNumber - 4
        }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded.value.not()) stringResource(R.string.art_image) else ""
        ),
        onBackHandler = { navigator.navigateBack() }
    ) {
        LaunchedEffect(expanded, artworks) {
            if (artworks != null) {
                lastExpandedState = expanded
            }
        }

        LaunchedEffect(gridState, titleHeight) {
            delay(1.seconds)
            snapshotFlow { gridState.isScrollInProgress }
                .distinctUntilChanged()
                .filter { it.not() }
                .collect {
                    if (titleHeight == 0 || gridState.firstVisibleItemIndex > 0) return@collect

                    val threshold = titleHeight / 2
                    val currentOffset = gridState.firstVisibleItemScrollOffset

                    if (currentOffset in 1 until titleHeight) {
                        val targetIndex = if (currentOffset < threshold) 0 else 1
                        gridState.animateScrollToItem(targetIndex)
                    }
                }
        }

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value) {
                loadNextPage()
            }
        }

        LaunchedEffect(navigator) {
            navigator.navResult.collect { url ->
                if (url is String) {
                    val timeStamp = changeToCurrentDate(currentTimeMillis())
                    val fileName = "MEVER_ART_${timeStamp}.jpg"
                    startDownload(url, fileName)
                    navigator.navigate(GalleryLandingRoute)
                }
            }
        }

        LaunchedEffect(query) {
            snapshotFlow { query }
                .debounce(1.seconds)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { search ->
                    if (lastQuery != search) {
                        lastQuery = search
                        getArtworks(search)
                    }
                }
        }

        LaunchedEffect(artResponseState) {
            artResponseState.handleUiState(
                onLoading = {
                    if (currentPage == 1) {
                        artworks = null
                    }
                    errorMessage = ""
                },
                onSuccess = {
                    if (currentPage == 1) {
                        artworks = it
                    }
                },
                onFailed = {
                    if (currentPage == 1) {
                        artworks = null
                    }
                    errorMessage = it.orEmpty()
                }
            )
        }

        MeverDialog(
            showDialog = errorMessage.isNotEmpty(),
            description = errorMessage,
            onClickPrimaryAction = {
                errorMessage = ""
                getArtworks(query)
            },
            onClickSecondaryAction = { navigator.navigateBack() }
        )

        MeverBottomSheet(
            showBottomSheet = selectedArtwork != null,
            isAlwaysRectangular = true,
            isDisableContentDrag = true,
            shouldDismissOnClickOutside = true,
            onDismissBottomSheet = { selectedArtwork = null }
        ) {
            selectedArtwork?.let { art ->
                ArtworkDetailSheetContent(
                    artwork = art,
                    onClickPreview = {
                        val currentList = artworks.orEmpty()
                        val initialIdx = currentList.indexOfFirst { it.id == art.id }.coerceAtLeast(0)
                        selectedArtwork = null
                        navigator.navigate(
                            GalleryContentDetailRoute(
                                contents = currentList[initialIdx].let { selectedArt ->
                                    listOf(
                                        Content(
                                            id = initialIdx,
                                            isVideo = false,
                                            fileName = "MEVER_ART_${selectedArt.id}_${changeToCurrentDate(currentTimeMillis())}.jpg",
                                            media = selectedArt.imageUrl,
                                            isDownloadable = true,
                                            isPreview = true
                                        )
                                    )
                                },
                                initialIndex = 0
                            )
                        )
                    },
                    onClickDownload = {
                        val timeStamp = changeToCurrentDate(currentTimeMillis())
                        val fileName = "MEVER_ART_${art.id}_${timeStamp}.jpg"
                        startDownload(art.imageUrl, fileName)
                        selectedArtwork = null
                        navigator.navigate(GalleryLandingRoute)
                    }
                )
            }
        }

        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp64)
                    .fadingEdge(
                        side = Bottom,
                        isVisible = showBottomFade
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { titleHeight = it.size.height }
                ) {
                    Spacer(modifier = Modifier.height(Dp16))
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = Dp24),
                            verticalArrangement = spacedBy(Dp8)
                        ) {
                            Text(
                                text = stringResource(R.string.art_image),
                                style = typography.h2.copy(fontSize = Sp32),
                                color = colors.blackWhite
                            )
                            Text(
                                modifier = Modifier.padding(bottom = Dp24),
                                text = stringResource(R.string.art_image_desc),
                                style = typography.body2,
                                color = colors.grayLightGray
                            )
                        }
                    }
                    MeverTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24)
                            .background(colors.whiteDark),
                        context = context,
                        value = TextFieldValue(query),
                        leadingIcon = R.drawable.ic_search,
                        hint = R.string.keyword_hint,
                        onValueChange = { query = it.text },
                        onClickLeadingIcon = {}
                    )
                    Spacer(modifier = Modifier.height(Dp24))
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(Dp3),
                    thickness = Dp1,
                    color = colors.blackWhite.copy(alpha = 0.12f)
                )
                Spacer(modifier = Modifier.height(Dp1))
                artworks?.let { result ->
                    if (result.isNotEmpty()) LazyVerticalStaggeredGrid(
                        modifier = Modifier.fillMaxSize(),
                        state = gridState,
                        columns = if (deviceType == PHONE) StaggeredGridCells.Fixed(2)
                        else StaggeredGridCells.Adaptive(Dp150),
                        contentPadding = PaddingValues(
                            start = Dp24,
                            end = Dp24,
                            top = Dp24,
                            bottom = Dp160
                        ),
                        verticalItemSpacing = Dp16,
                        horizontalArrangement = spacedBy(Dp16)
                    ) {
                        itemsIndexed(
                            items = result,
                            key = { _, item -> item.id },
                            contentType = { _, item -> item.imageUrl }
                        ) { index, item ->
                            ArtworkCard(
                                artwork = item,
                                index = index,
                                onClick = { selectedArtwork = item }
                            )
                        }

                        if (isPagingLoading) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = Dp16),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(Dp24),
                                        color = colors.alwaysPurple,
                                        strokeWidth = Dp2
                                    )
                                }
                            }
                        }
                    } else ArtworkNotFoundComponent()
                } ?: LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dp24),
                    columns = if (deviceType == PHONE) GridCells.Fixed(2)
                    else GridCells.Adaptive(Dp150),
                    contentPadding = PaddingValues(vertical = Dp24),
                    verticalArrangement = spacedBy(Dp16),
                    horizontalArrangement = spacedBy(Dp16)
                ) {
                    items(20) {
                        Box(
                            modifier = Modifier
                                .size(Dp150)
                                .clip(RoundedCornerShape(Dp8))
                                .meverShimmer()
                        )
                    }
                }
            }
            MeverBannerAd(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun ArtworkCard(
    artwork: ArtEntity,
    index: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = DpHalf, shape = RoundedCornerShape(Dp16))
            .background(color = colors.whiteDarkGray, shape = RoundedCornerShape(Dp16))
            .border(
                width = DpHalf,
                color = colors.blackWhite.copy(alpha = 0.08f),
                shape = RoundedCornerShape(Dp16)
            )
            .clip(RoundedCornerShape(Dp16))
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            MeverImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (index % 3 == 0) 0.85f else if (index % 2 == 0) 1.25f else 1.0f)
                    .clip(RoundedCornerShape(topStart = Dp16, topEnd = Dp16)),
                source = artwork.thumbnailUrl.ifEmpty { artwork.imageUrl }
            )
            if (artwork.dateDisplay.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(TopEnd)
                        .padding(Dp8)
                        .background(
                            color = MeverBlack.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(Dp12)
                        )
                        .padding(horizontal = Dp8, vertical = Dp4)
                ) {
                    Text(
                        text = artwork.dateDisplay,
                        style = typography.label3.copy(fontSize = Sp10),
                        color = colors.alwaysWhite
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(Dp12),
            verticalArrangement = spacedBy(Dp4)
        ) {
            Text(
                text = artwork.title,
                style = typography.bodyBold2,
                color = colors.blackWhite,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (artwork.placeOfOrigin.isNotEmpty()) {
                Text(
                    text = artwork.placeOfOrigin,
                    style = typography.label3,
                    color = colors.grayLightGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ArtworkDetailSheetContent(
    artwork: ArtEntity,
    onClickPreview: () -> Unit,
    onClickDownload: () -> Unit
) {
    val scrollState = rememberScrollState()
    val showTopDivider by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = showTopDivider,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = Dp1,
                color = colors.blackWhite.copy(alpha = 0.12f)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(top = Dp24, start = Dp24, end = Dp24),
            verticalArrangement = spacedBy(Dp16)
        ) {
            MeverImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dp250)
                    .clip(RoundedCornerShape(Dp12)),
                source = artwork.imageUrl
            )
            Text(
                text = artwork.title,
                style = typography.h3,
                color = colors.blackWhite
            )
            if (artwork.artistDisplay.isNotEmpty()) {
                Text(
                    text = artwork.artistDisplay,
                    style = typography.bodyBold2,
                    color = colors.alwaysPurple
                )
            }
            if (artwork.dateDisplay.isNotEmpty() || artwork.mediumDisplay.isNotEmpty()) {
                Text(
                    text = listOfNotNull(
                        artwork.dateDisplay.takeIf { it.isNotBlank() },
                        artwork.mediumDisplay.takeIf { it.isNotBlank() },
                        artwork.placeOfOrigin.takeIf { it.isNotBlank() }
                    ).joinToString(" • "),
                    style = typography.body3,
                    color = colors.grayLightGray
                )
            }
            val cleanDescription = remember(artwork.description) {
                artwork.description.replace(Regex("<[^>]*>"), "").trim()
            }
            if (cleanDescription.isNotEmpty()) {
                Text(
                    text = cleanDescription,
                    style = typography.body2,
                    color = colors.blackWhite
                )
            }
            Spacer(modifier = Modifier.height(Dp24))
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = Dp1,
            color = colors.blackWhite.copy(alpha = 0.12f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dp24, end = Dp24, top = Dp16, bottom = Dp32),
            horizontalArrangement = spacedBy(Dp12)
        ) {
            MeverButton(
                modifier = Modifier
                    .weight(1f)
                    .height(Dp48),
                title = stringResource(R.string.fullscreen_view),
                buttonType = MeverButtonType.Outlined(
                    contentColor = colors.alwaysPurple,
                    borderColor = colors.alwaysPurple
                ),
                onClick = onClickPreview
            )
            MeverButton(
                modifier = Modifier
                    .weight(1f)
                    .height(Dp48),
                title = stringResource(R.string.download),
                buttonType = MeverButtonType.Filled(
                    backgroundColor = colors.alwaysPurple,
                    contentColor = colors.alwaysWhite
                ),
                onClick = onClickDownload
            )
        }
    }
}

@Composable
private fun ArtworkNotFoundComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(Dp64),
            painter = painterResource(R.drawable.ic_clear),
            colorFilter = tint(colors.blackWhite.copy(0.4f)),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(Dp16))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.no_result),
            textAlign = TextAlign.Center,
            style = typography.body1.copy(fontSize = Sp18),
            color = colors.blackWhite.copy(0.4f)
        )
    }
}
