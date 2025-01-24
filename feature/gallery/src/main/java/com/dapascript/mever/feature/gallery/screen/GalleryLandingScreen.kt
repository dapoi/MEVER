package com.dapascript.mever.feature.gallery.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp210
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.isAvailableOnLocal
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.R
import com.dapascript.mever.feature.gallery.navigation.route.GalleryContentDetailRoute
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.DELETE_ALL
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.MORE
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.listDropDown
import com.dapascript.mever.feature.gallery.viewmodel.GalleryLandingViewModel
import com.ketch.DownloadModel
import com.ketch.Status.SUCCESS
import com.dapascript.mever.core.common.R as RCommon

@Composable
internal fun GalleryLandingScreen(
    navigator: BaseNavigator,
    viewModel: GalleryLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }

    BaseScreen(
        topBarArgs = MeverTopBarAttr.TopBarArgs(
            actionMenus = if (downloadList.isNullOrEmpty().not()) listOf(ActionMenu(
                icon = R.drawable.ic_more,
                nameIcon = MORE,
                onClickActionMenu = { showDropDownMenu = true }
            )) else emptyList(),
            screenName = GALLERY,
            onClickBack = { navigator.popBackStack() }
        ),
        allowScreenOverlap = true
    ) {
        LaunchedEffect(Unit) { getAllDownloads() }

        PopUpDropdownMenu(
            listDropDown = listDropDown,
            showDropDownMenu = showDropDownMenu,
            onShowDeleteAllDialog = { showDeleteAllDialog = it },
            onDismissDropDownMenu = { showDropDownMenu = it }
        )

        GalleryContentSection(
            downloadList = downloadList?.filter { download ->
                with(download) {
                    (isAvailableOnLocal() || status != SUCCESS) && (selectedFilter == UNKNOWN || tag == selectedFilter.platformName)
                }
            },
            platformTypes = platformTypes,
            selectedFilter = selectedFilter,
            onClickFilter = { selectedFilter = it },
            onClickCard = { model ->
                with(model) {
                    if (progress < 100) when (status) {
                        com.ketch.Status.FAILED -> ketch.retry(id)
                        com.ketch.Status.PAUSED -> ketch.resume(id)
                        else -> ketch.pause(id)
                    } else navigator.navigate(
                        GalleryContentDetailRoute(
                            id = id,
                            sourceFile = getMeverFiles()?.find { file ->
                                file.name == fileName
                            }?.path.orEmpty(),
                            fileName = fileName.replaceTimeFormat()
                        )
                    )
                }
            },
            onClickShare = {
                shareContent(
                    context = context,
                    authority = context.packageName,
                    path = getMeverFiles()?.find { file -> file.name == it.fileName }?.path.orEmpty()
                )
            },
            onClickDelete = { showDeleteDialog = it.id },
            onChangeFilter = { selectedFilter = it }
        )

        MeverDialog(
            showDialog = showDeleteAllDialog,
            meverDialogArgs = MeverDialogArgs(
                title = "Delete all files?",
                primaryButtonText = "Delete",
                onClickPrimaryButton = {
                    deleteAllDownloads()
                    showDeleteAllDialog = false
                },
                onClickSecondaryButton = { showDeleteAllDialog = false }
            )
        ) {
            Text(
                text = "All files that have been deleted cannot be recovered",
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }

        showDeleteDialog?.let { id ->
            MeverDialog(
                showDialog = true,
                meverDialogArgs = MeverDialogArgs(
                    title = "Delete this file?",
                    primaryButtonText = "Delete",
                    onClickPrimaryButton = {
                        deleteDownload(id)
                        showDeleteDialog = null
                    },
                    onClickSecondaryButton = { showDeleteDialog = null }
                )
            ) {
                Text(
                    text = "File that has been deleted cannot be recovered",
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
    platformTypes: List<PlatformType>,
    selectedFilter: PlatformType,
    downloadList: List<DownloadModel>?,
    modifier: Modifier = Modifier,
    onClickFilter: (PlatformType) -> Unit,
    onClickCard: (DownloadModel) -> Unit,
    onClickShare: (DownloadModel) -> Unit,
    onClickDelete: (DownloadModel) -> Unit,
    onChangeFilter: (PlatformType) -> Unit
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(top = Dp64)
        .then(Modifier.systemBarsPadding()),
    horizontalAlignment = CenterHorizontally
) {
    downloadList?.let { files ->
        AnimatedContent(
            targetState = files.isNotEmpty(),
            label = "Contents"
        ) { isNotEmpty ->
            if (isNotEmpty) {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Dp48)
                    ) {
                        if (platformTypes.size > 1) item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(PaddingValues(horizontal = Dp24)),
                                horizontalArrangement = spacedBy(Dp8),
                                verticalAlignment = CenterVertically
                            ) {
                                MeverButton(
                                    title = "All",
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
                                    urlThumbnail = it.metaData
                                ),
                                onClickCard = { onClickCard(it) },
                                onClickShare = { onClickShare(it) },
                                onClickDelete = { onClickDelete(it) }
                            )
                        }
                    }
                }
            } else MeverEmptyItem(
                image = RCommon.drawable.ic_gallery_empty,
                size = Dp210,
                description = "Looks like there’s nothing here... Download something to get content!"
            )
        }
    }

    downloadList?.let {
        LaunchedEffect(selectedFilter, downloadList.isEmpty()) {
            if (selectedFilter != UNKNOWN && downloadList.isEmpty()) onChangeFilter(UNKNOWN)
        }
    }
}

@Composable
private fun PopUpDropdownMenu(
    listDropDown: List<String>,
    showDropDownMenu: Boolean,
    onShowDeleteAllDialog: (Boolean) -> Unit,
    onDismissDropDownMenu: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = showDropDownMenu,
            containerColor = colorScheme.background,
            onDismissRequest = { onDismissDropDownMenu(false) }
        ) {
            listDropDown.map { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = typography.body1,
                            color = colorScheme.onPrimary
                        )
                    },
                    onClick = {
                        when (item) {
                            DELETE_ALL -> onShowDeleteAllDialog(true)
                        }
                        onDismissDropDownMenu(false)
                    }
                )
            }
        }
    }
}