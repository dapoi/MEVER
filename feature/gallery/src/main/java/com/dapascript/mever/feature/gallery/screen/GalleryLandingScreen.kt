package com.dapascript.mever.feature.gallery.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverLabel
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp210
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.R
import com.dapascript.mever.feature.gallery.navigation.route.GalleryContentViewerRoute
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.DELETE_ALL
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.FILTER_BY_CATEGORIES
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.MORE
import com.dapascript.mever.feature.gallery.screen.attr.GalleryLandingScreenAttr.listDropDown
import com.dapascript.mever.feature.gallery.viewmodel.GalleryLandingViewModel
import com.ketch.DownloadModel
import com.dapascript.mever.core.common.R as RCommon

@Composable
internal fun GalleryLandingScreen(
    navigator: BaseNavigator,
    viewModel: GalleryLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    val matchingPlatforms = PlatformType.entries.filter { platType ->
        downloadList.any { it.tag == platType.platformName }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(
            actionMenus = if (downloadList.isNotEmpty()) listOf(ActionMenu(
                icon = R.drawable.ic_more,
                nameIcon = MORE,
                onClickActionMenu = { showDropDownMenu = true }
            )) else emptyList(),
            screenName = GALLERY,
            onClickBack = { navigator.popBackStack() }
        ),
        allowScreenOverlap = downloadList.isEmpty()
    ) {
        LaunchedEffect(Unit) { getAllDownloads() }

        PopUpDropdownMenu(
            listDropDown = if (matchingPlatforms.size == 1) listDropDown.dropLast(1) else listDropDown,
            showDropDownMenu = showDropDownMenu,
            onShowDeleteAllDialog = { showDeleteAllDialog = it },
            onShowFilterDialog = { showFilterDialog = it },
            onDismissDropDownMenu = { showDropDownMenu = it }
        )

        GalleryContentSection(
            downloadList = downloadList.filter { selectedFilter == UNKNOWN || it.tag == selectedFilter.platformName },
            selectedFilter = selectedFilter,
            onClearFilterClick = { selectedFilter = UNKNOWN },
            onContentClick = { fileName ->
                navigator.navigate(
                    GalleryContentViewerRoute(
                        sourceFile = getMeverFiles()?.find { file ->
                            file.name == fileName
                        }?.path.orEmpty(),
                        fileName = fileName.replaceTimeFormat()
                    )
                )
            },
            onShareClick = { fileName ->
                shareContent(
                    context = context,
                    authority = context.packageName + ".provider",
                    path = getMeverFiles()?.find { file -> file.name == fileName }?.path.orEmpty()
                )
            },
            onDeleteClick = { showDeleteDialog = it }
        )

        MeverDialog(
            showDialog = showDeleteAllDialog,
            meverDialogArgs = MeverDialogArgs(
                title = "Delete all files?",
                primaryButtonText = "Delete",
                onActionClick = {
                    deleteAllDownloads()
                    showDeleteAllDialog = false
                },
                onDimissClick = { showDeleteAllDialog = false }
            )
        ) {
            Text(
                text = "All files that have been deleted cannot be recovered",
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }

        MeverDialog(
            showDialog = showFilterDialog,
            meverDialogArgs = MeverDialogArgs(
                title = "Categories",
                primaryButtonText = "Apply",
                onDimissClick = { showFilterDialog = false }
            ),
            hideInteractionButton = true
        ) {
            matchingPlatforms.map { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dp4)
                        .clickable {
                            selectedFilter = type
                            showFilterDialog = false
                        },
                    horizontalArrangement = spacedBy(Dp8),
                    verticalAlignment = CenterVertically
                ) {
                    MeverRadioButton(
                        isChecked = selectedFilter == type,
                        onCheckedChange = {
                            selectedFilter = type
                            showFilterDialog = false
                        }
                    )
                    Text(
                        text = type.platformName,
                        style = typography.body1,
                        color = colorScheme.onPrimary
                    )
                }
            }
        }

        showDeleteDialog?.let { id ->
            MeverDialog(
                showDialog = true,
                meverDialogArgs = MeverDialogArgs(
                    title = "Delete this file?",
                    primaryButtonText = "Delete",
                    onActionClick = {
                        deleteDownload(id)
                        showDeleteDialog = null
                        if (downloadList.all { it.tag != selectedFilter.name }) selectedFilter = UNKNOWN
                    },
                    onDimissClick = { showDeleteDialog = null }
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
    downloadList: List<DownloadModel>,
    selectedFilter: PlatformType,
    onClearFilterClick: () -> Unit,
    onContentClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    if (downloadList.isNotEmpty()) CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = if (selectedFilter != UNKNOWN) Dp48 else Dp0,
                    bottom = Dp48
                )
            ) {
                items(
                    items = downloadList,
                    key = { it.id }
                ) {
                    MeverCard(
                        modifier = Modifier
                            .padding(vertical = Dp12)
                            .animateItem(),
                        meverCardArgs = MeverCardArgs(
                            image = it.url,
                            tag = it.tag,
                            metaData = it.metaData,
                            fileName = it.fileName,
                            status = it.status,
                            progress = it.progress,
                            total = it.total,
                            path = it.path,
                            type = DOWNLOADED,
                            onPlayClick = { onContentClick(it.fileName) },
                            onShareContentClick = { onShareClick(it.fileName) },
                            onDeleteContentClick = { onDeleteClick(it.id) }
                        )
                    )
                }
            }
            AnimatedVisibility(
                visible = selectedFilter != UNKNOWN,
                enter = fadeIn() + slideInVertically(),
                exit = slideOutVertically() + fadeOut()
            ) {
                MeverLabel(
                    message = "Filter by ${selectedFilter.platformName}",
                    actionMessage = "Clear",
                    onActionLabelClick = onClearFilterClick
                )
            }
        }
    } else MeverEmptyItem(
        image = RCommon.drawable.ic_gallery_empty,
        size = Dp210,
        description = "Looks like there’s nothing here... Download something to get content!"
    )
}

@Composable
private fun PopUpDropdownMenu(
    listDropDown: List<String>,
    showDropDownMenu: Boolean,
    onShowDeleteAllDialog: (Boolean) -> Unit,
    onShowFilterDialog: (Boolean) -> Unit,
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
                            FILTER_BY_CATEGORIES -> onShowFilterDialog(true)
                        }
                        onDismissDropDownMenu(false)
                    }
                )
            }
        }
    }
}