package com.dapascript.mever.feature.gallery.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.spacedBy
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIcon
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIconBackgroundColor
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverPopupDropDownMenu
import com.dapascript.mever.core.common.ui.component.MeverSnackbar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.DELETE_ALL
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.MORE
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.PAUSE_ALL
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.RESUME_ALL
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.listDropDown
import com.dapascript.mever.feature.gallery.viewmodel.GalleryLandingViewModel
import com.ketch.DownloadModel
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.SUCCESS
import com.dapascript.mever.core.common.R as RCommon

@Composable
internal fun GalleryLandingScreen(
    navController: NavController,
    viewModel: GalleryLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val downloadList = downloadList.collectAsStateValue()
    val scrollState = rememberScrollState()
    val isExpanded by remember { derivedStateOf { scrollState.value <= titleHeight } }
    var snackbarMessage by remember { mutableStateOf("") }
    var showFailedDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            actionMenus = if (downloadList.isNullOrEmpty().not()) listOf(
                ActionMenu(
                    icon = R.drawable.ic_more,
                    nameIcon = MORE,
                    onClickActionMenu = { showDropDownMenu = true }
                )) else emptyList(),
            screenName = if (isExpanded.not()) stringResource(RCommon.string.gallery) else "",
            onClickBack = { navController.popBackStack() }
        ),
        allowScreenOverlap = true
    ) {
        MeverPopupDropDownMenu(
            modifier = Modifier.padding(top = Dp64, end = Dp24),
            listDropDown = listDropDown.filter {
                when (it) {
                    DELETE_ALL -> downloadList.isNullOrEmpty().not()
                    PAUSE_ALL -> downloadList?.any { model ->
                        model.status == PROGRESS
                    } == true

                    RESUME_ALL -> downloadList?.any { model ->
                        model.progress < model.total && model.status == PAUSED
                    } == true

                    else -> false
                }
            },
            showDropDownMenu = showDropDownMenu,
            onDismissDropDownMenu = { showDropDownMenu = it },
            onClick = { item ->
                when (item) {
                    DELETE_ALL -> showDeleteAllDialog = true
                    PAUSE_ALL -> ketch.pauseAll()
                    else -> downloadList?.filter { model -> model.status == PAUSED }?.map { model ->
                        ketch.resume(model.id)
                    }
                }
            }
        )

        GalleryContentSection(
            downloadList = downloadList?.filter { download ->
                selectedFilter == UNKNOWN || download.tag == selectedFilter.platformName
            },
            scrollState = scrollState,
            isExpanded = isExpanded,
            platformTypes = platformTypes,
            selectedFilter = selectedFilter,
            onClickFilter = { selectedFilter = it },
            onClickCard = { model ->
                with(model) {
                    when (status) {
                        SUCCESS -> navController.navigateTo(
                            GalleryContentDetailRoute(
                                id = id,
                                sourceFile = getMeverFiles()?.find { file ->
                                    file.name == fileName
                                }?.path.orEmpty(),
                                fileName = fileName.replaceTimeFormat()
                            )
                        )

                        FAILED -> showFailedDialog = id
                        PAUSED -> ketch.resume(id)
                        else -> ketch.pause(id)
                    }
                }
            },
            onClickShare = {
                shareContent(
                    context = context,
                    authority = context.packageName,
                    path = getMeverFiles()?.find { file ->
                        file.name == it.fileName
                    }?.path.orEmpty()
                )
            },
            onClickDelete = { showDeleteDialog = it.id },
            onChangeFilter = { selectedFilter = it },
            onChangeTitleHeight = { titleHeight = it }
        )

        MeverSnackbar(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dp24)
                .navigationBarsPadding(),
            message = snackbarMessage
        )

        MeverDialog(
            showDialog = showDeleteAllDialog,
            meverDialogArgs = MeverDialogArgs(
                title = stringResource(R.string.delete_all_title),
                primaryButtonText = stringResource(R.string.delete_button),
                onClickPrimaryButton = {
                    ketch.clearAllDb()
                    snackbarMessage = context.getString(R.string.snackbar_delete)
                    showDeleteAllDialog = false
                },
                onClickSecondaryButton = { showDeleteAllDialog = false }
            )
        ) {
            Text(
                text = stringResource(R.string.delete_all_desc),
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }

        showDeleteDialog?.let { id ->
            MeverDialog(
                showDialog = true,
                meverDialogArgs = MeverDialogArgs(
                    title = stringResource(R.string.delete_title),
                    primaryButtonText = stringResource(R.string.delete_button),
                    onClickPrimaryButton = {
                        ketch.clearDb(id)
                        showDeleteDialog = null
                    },
                    onClickSecondaryButton = { showDeleteDialog = null }
                )
            ) {
                Text(
                    text = stringResource(R.string.delete_desc),
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
            }
        }

        showFailedDialog?.let { id ->
            MeverDialog(
                showDialog = true,
                meverDialogArgs = MeverDialogArgs(
                    title = stringResource(R.string.download_failed_title),
                    primaryButtonText = stringResource(R.string.delete_button),
                    secondaryButtonText = stringResource(R.string.retry),
                    onClickPrimaryButton = {
                        ketch.clearDb(id)
                        showFailedDialog = null
                    },
                    onClickSecondaryButton = {
                        ketch.retry(id)
                        showFailedDialog = null
                    }
                )
            ) {
                Text(
                    text = stringResource(R.string.download_failed_desc),
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GalleryContentSection(
    selectedFilter: PlatformType,
    scrollState: ScrollState,
    isExpanded: Boolean,
    platformTypes: List<PlatformType>,
    downloadList: List<DownloadModel>?,
    modifier: Modifier = Modifier,
    onClickFilter: (PlatformType) -> Unit,
    onClickCard: (DownloadModel) -> Unit,
    onClickShare: (DownloadModel) -> Unit,
    onClickDelete: (DownloadModel) -> Unit,
    onChangeFilter: (PlatformType) -> Unit,
    onChangeTitleHeight: (Int) -> Unit
) = BoxWithConstraints(
    modifier = modifier
        .fillMaxSize()
        .padding(top = Dp64)
        .systemBarsPadding()
) {
    Column(
        modifier = Modifier
            .matchParentSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(Dp16))
        Text(
            text = stringResource(RCommon.string.gallery),
            style = typography.h2.copy(fontSize = Sp32),
            color = colorScheme.onPrimary,
            modifier = Modifier
                .padding(PaddingValues(start = Dp24))
                .onGloballyPositioned { onChangeTitleHeight(it.size.height) }
        )
        if (platformTypes.size > 1) Spacer(modifier = Modifier.height(Dp32))
        Column(modifier = Modifier.height(this@BoxWithConstraints.maxHeight)) {
            if (platformTypes.size > 1) {
                FilterContent(
                    modifier = Modifier
                        .background(colorScheme.background)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(start = Dp24, end = Dp24, top = Dp4, bottom = Dp24),
                    platformTypes = platformTypes,
                    selectedFilter = selectedFilter,
                ) { onClickFilter(it) }
            }
            if (isExpanded.not()) HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp1)
                    .shadow(Dp3),
                thickness = Dp1,
                color = colorScheme.onPrimary.copy(alpha = 0.12f)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .nestedScroll(object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ) = if (available.y > 0 || isExpanded) Offset.Zero
                        else Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                    }
                    )
            ) {
                downloadList?.let { files ->
                    LaunchedEffect(selectedFilter, downloadList.isEmpty()) {
                        if (selectedFilter != UNKNOWN && downloadList.isEmpty()) onChangeFilter(
                            UNKNOWN
                        )
                    }
                    AnimatedContent(
                        targetState = files.isNotEmpty(),
                        label = "Contents"
                    ) { isNotEmpty ->
                        if (isNotEmpty) {
                            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(
                                        items = downloadList,
                                        key = { it.id }
                                    ) {
                                        MeverCard(
                                            modifier = Modifier
                                                .padding(horizontal = Dp24)
                                                .animateItem(),
                                            cardArgs = MeverCardArgs(
                                                source = it.url,
                                                tag = it.tag,
                                                fileName = it.fileName,
                                                status = it.status,
                                                progress = it.progress,
                                                total = it.total,
                                                path = it.path,
                                                urlThumbnail = it.metaData,
                                                icon = getPlatformIcon(it.tag),
                                                iconBackgroundColor = getPlatformIconBackgroundColor(
                                                    it.tag
                                                ),
                                                iconSize = Dp24,
                                                iconPadding = Dp5
                                            ),
                                            onClickCard = { onClickCard(it) },
                                            onClickShare = { onClickShare(it) },
                                            onClickDelete = { onClickDelete(it) }
                                        )
                                    }
                                }
                            }
                        } else MeverEmptyItem(
                            image = RCommon.drawable.ic_not_found,
                            size = Dp189,
                            description = stringResource(RCommon.string.empty_list_desc)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterContent(
    platformTypes: List<PlatformType>,
    selectedFilter: PlatformType,
    modifier: Modifier = Modifier,
    onClickFilter: (PlatformType) -> Unit
) {
    if (platformTypes.size > 1) {
        Row(
            modifier = modifier,
            horizontalArrangement = spacedBy(Dp8),
            verticalAlignment = CenterVertically
        ) {
            MeverButton(
                title = stringResource(RCommon.string.all),
                shape = RoundedCornerShape(Dp64),
                buttonType = if (selectedFilter == UNKNOWN) FILLED else OUTLINED,
            ) { onClickFilter(UNKNOWN) }
            platformTypes.map { type ->
                MeverButton(
                    title = type.platformName,
                    shape = RoundedCornerShape(Dp64),
                    buttonType = if (selectedFilter == type) FILLED else OUTLINED,
                ) { onClickFilter(type) }
            }
        }
    }
}