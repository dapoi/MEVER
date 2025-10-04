package com.dapascript.mever.feature.explore.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialogError
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.feature.explore.viewmodel.ExploreLandingViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
@Composable
internal fun ExploreLandingScreen(
    navController: NavController,
    deviceType: DeviceType,
    viewModel: ExploreLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val exploreResponseState = exploreResponseState.collectAsStateValue()
    var contents by remember { mutableStateOf<List<ContentEntity>?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var lastQuery by rememberSaveable { mutableStateOf("") }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = stringResource(R.string.explore),
            onClickBack = { navController.popBackStack() }
        ),
        allowScreenOverlap = true
    ) {
        LaunchedEffect(query) {
            snapshotFlow { query }
                .debounce(1000)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest {
                    if (it.isNotEmpty() && lastQuery != it) {
                        lastQuery = it
                        getExploreContents(it)
                    }
                }
        }

        LaunchedEffect(exploreResponseState) {
            exploreResponseState.handleUiState(
                onLoading = {
                    contents = null
                    errorMessage = ""
                },
                onSuccess = { contents = it },
                onFailed = {
                    contents = null
                    errorMessage = it.orEmpty()
                }
            )
        }

        MeverDialogError(
            showDialog = errorMessage.isNotEmpty(),
            errorTitle = stringResource(R.string.error_title),
            errorDescription = errorMessage,
            onClickPrimary = {
                errorMessage = ""
                getExploreContents(query)
            },
            onClickSecondary = {
                contents = emptyList()
                errorMessage = ""
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dp64)
        ) {
            MeverTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp24)
                    .background(colorScheme.background),
                context = context,
                value = TextFieldValue(query),
                leadingIcon = R.drawable.ic_search,
                hint = R.string.keyword_hint,
                onValueChange = { query = it.text }
            )
            Spacer(modifier = Modifier.height(Dp16))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(Dp3),
                thickness = Dp1,
                color = colorScheme.onPrimary.copy(alpha = 0.12f)
            )
            Spacer(modifier = Modifier.height(Dp1))
            contents?.let { result ->
                if (result.isNotEmpty()) LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = if (deviceType == PHONE) StaggeredGridCells.Fixed(2)
                    else StaggeredGridCells.Adaptive(Dp150),
                    contentPadding = PaddingValues(Dp24),
                    verticalItemSpacing = Dp16,
                    horizontalArrangement = spacedBy(Dp16)
                ) {
                    itemsIndexed(
                        items = result,
                        key = { _, item -> item.id },
                        contentType = { _, item -> item.url }
                    ) { index, item ->
                        MeverImage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dp8))
                                .onCustomClick {
                                    navController.navigateTo(
                                        GalleryContentDetailRoute(
                                            contents = result.mapIndexed { contentIndex, contentEntity ->
                                                Content(
                                                    id = contentIndex,
                                                    isVideo = false,
                                                    primaryContent = contentEntity.url,
                                                    secondaryContent = contentEntity.previewUrl,
                                                    fileName = contentEntity.fileName
                                                )
                                            },
                                            initialIndex = index
                                        )
                                    )
                                },
                            source = item.previewUrl
                        )
                    }
                } else KeywordNotFoundComponent()
            } ?: LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dp24),
                columns = if (deviceType == PHONE) GridCells.Fixed(2)
                else GridCells.Adaptive(Dp150),
                contentPadding = PaddingValues(Dp0),
                verticalArrangement = spacedBy(Dp16),
                horizontalArrangement = spacedBy(Dp16)
            ) {
                items(20) {
                    Box(
                        modifier = Modifier
                            .size(Dp150)
                            .clip(RoundedCornerShape(Dp8))
                            .background(meverShimmer())
                    )
                }
            }
        }
    }
}

@Composable
private fun KeywordNotFoundComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(Dp64),
            painter = painterResource(R.drawable.ic_clear),
            colorFilter = tint(colorScheme.onPrimary.copy(0.4f)),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(Dp16))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.no_result),
            textAlign = TextAlign.Center,
            style = typography.body1.copy(fontSize = Sp18),
            color = colorScheme.onPrimary.copy(0.4f)
        )
    }
}