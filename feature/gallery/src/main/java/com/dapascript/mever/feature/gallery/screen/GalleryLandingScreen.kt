package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
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
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.getFilePath
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
import kotlinx.coroutines.launch
import java.io.File
import com.dapascript.mever.core.common.R as RCommon

@Composable
internal fun GalleryLandingScreen(
    navController: NavController,
    viewModel: GalleryLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val downloadList = downloadList.collectAsStateValue()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex < 1 } }
    var showFailedDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            actionMenus = if (downloadList.isNullOrEmpty().not() && isExpanded.not()) listOf(
                ActionMenu(
                    icon = R.drawable.ic_more,
                    nameIcon = MORE,
                    onClickActionMenu = { showDropDownMenu = true }
                )) else emptyList(),
            title = if (isExpanded.not()) stringResource(RCommon.string.gallery) else "",
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
            selectedFilter = selectedFilter,
            listState = listState,
            isExpanded = isExpanded,
            platformTypes = platformTypes,
            downloadList = downloadList?.filter { download ->
                selectedFilter == UNKNOWN || download.tag == selectedFilter.platformName
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64)
                .systemBarsPadding(),
            onClickFilter = {
                scope.launch {
                    listState.animateScrollToItem(0)
                    if (listState.firstVisibleItemIndex == 0) selectedFilter = it
                }
            },
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
                        PAUSED -> ketch.resume(id)
                        else -> ketch.pause(id)
                    }
                }
            },
            onClickShare = {
                shareContent(
                    context = context,
                    file = File(getFilePath(it.fileName))
                )
            },
            onClickDelete = { showDeleteDialog = it.id },
            onChangeFilter = {
                scope.launch {
                    listState.animateScrollToItem(0)
                    if (listState.firstVisibleItemIndex == 0) selectedFilter = it
                }
            }
        )

        MeverDialog(
            showDialog = showDeleteAllDialog,
            meverDialogArgs = MeverDialogArgs(
                title = stringResource(R.string.delete_all_title),
                primaryButtonText = stringResource(R.string.delete_button),
                onClickPrimaryButton = {
                    scope.launch { listState.scrollToItem(0) }
                    ketch.clearAllDb()
                    showDeleteAllDialog = false
                },
                onClickSecondaryButton = { showDeleteAllDialog = false }
            )
        ) {
            Text(
                text = stringResource(R.string.delete_all_desc),
                textAlign = Center,
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
                    textAlign = Center,
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
                    textAlign = Center,
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun GalleryContentSection(
    selectedFilter: PlatformType,
    listState: LazyListState,
    isExpanded: Boolean,
    platformTypes: List<PlatformType>,
    downloadList: List<DownloadModel>?,
    modifier: Modifier = Modifier,
    onClickFilter: (PlatformType) -> Unit,
    onClickCard: (DownloadModel) -> Unit,
    onClickShare: (DownloadModel) -> Unit,
    onClickDelete: (DownloadModel) -> Unit,
    onChangeFilter: (PlatformType) -> Unit
) {
    LaunchedEffect(selectedFilter, downloadList) {
        if (selectedFilter != UNKNOWN && downloadList?.isEmpty() == true) onChangeFilter(UNKNOWN)
    }

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        downloadList?.let {
            if (downloadList.isNotEmpty()) LazyColumn(
                modifier = modifier,
                state = listState
            ) {
                item {
                    Text(
                        text = stringResource(RCommon.string.gallery),
                        style = typography.h2.copy(fontSize = Sp32),
                        color = colorScheme.onPrimary,
                        modifier = Modifier.padding(top = Dp16, start = Dp24, end = Dp24)
                    )
                }
                stickyHeader {
                    if (platformTypes.size > 1) FilterContent(
                        modifier = Modifier
                            .background(colorScheme.background)
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(start = Dp24, end = Dp24, top = Dp16, bottom = Dp24),
                        platformTypes = platformTypes,
                        selectedFilter = selectedFilter
                    ) { filter -> onClickFilter(filter) }
                    if (isExpanded.not()) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(Dp3),
                            thickness = Dp1,
                            color = colorScheme.onPrimary.copy(alpha = 0.12f)
                        )
                    }
                }
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
                            icon = if (it.tag.isNotEmpty()) {
                                getPlatformIcon(it.tag)
                            } else null,
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
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(RCommon.string.gallery),
                        style = typography.h2.copy(fontSize = Sp32),
                        color = colorScheme.onPrimary,
                        modifier = Modifier.padding(top = Dp80, start = Dp24, end = Dp24)
                    )
                    MeverEmptyItem(
                        image = R.drawable.ic_not_found,
                        size = Dp189,
                        description = stringResource(R.string.empty_list_desc)
                    )
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
                buttonType = getButtonType(selectedFilter == UNKNOWN),
            ) { onClickFilter(UNKNOWN) }
            platformTypes.map { type ->
                MeverButton(
                    title = type.platformName,
                    shape = RoundedCornerShape(Dp64),
                    buttonType = getButtonType(selectedFilter == type),
                ) { onClickFilter(type) }
            }
        }
    }
}

@Composable
private fun getButtonType(isFilled: Boolean) = if (isFilled) Filled(
    backgroundColor = colorScheme.primary,
    contentColor = MeverWhite
) else Outlined(
    borderColor = colorScheme.primary,
    contentColor = colorScheme.primary
)