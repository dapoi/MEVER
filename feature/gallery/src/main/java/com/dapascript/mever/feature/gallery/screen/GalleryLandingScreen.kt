package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.base.attr.BaseScreenAttr.BaseScreenArgs
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.replaceTimeFormat
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.navigation.route.GalleryContentViewerRoute
import com.dapascript.mever.feature.gallery.viewmodel.GalleryLandingViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GalleryLandingScreen(
    navigator: BaseNavigator,
    viewModel: GalleryLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val downloadList = downloadList.collectAsStateValue()
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }

    BaseScreen(
        baseScreenArgs = BaseScreenArgs(
            screenName = GALLERY,
            onClickBack = { navigator.popBackStack() }
        )
    ) {
        LaunchedEffect(Unit) { getAllDownloads() }

        if (downloadList.isNotEmpty()) CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                            onPlayClick = {
                                navigator.navigate(
                                    GalleryContentViewerRoute(
                                        sourceFile = getMeverFiles()?.find { file ->
                                            file.name == it.fileName
                                        }?.path.orEmpty(),
                                        fileName = it.fileName.replaceTimeFormat()
                                    )
                                )
                            },
                            onShareContentClick = {
                                shareContent(
                                    context = context,
                                    authority = context.packageName + ".provider",
                                    path = getMeverFiles()?.find { file -> file.name == it.fileName }?.path.orEmpty()
                                )
                            },
                            onDeleteContentClick = { showDeleteDialog = it.id }
                        )
                    )
                }
            }
        } else MeverEmptyItem(
            image = R.drawable.ic_gallery_empty,
            description = "You haven't downloaded any files yet"
        )
    }

    showDeleteDialog?.let { id ->
        MeverDialog(
            showDialog = true,
            meverDialogArgs = MeverDialogArgs(
                title = "Delete this file?",
                primaryButtonText = "Delete",
                onPrimaryButtonClick = {
                    deleteDownload(id)
                    showDeleteDialog = null
                },
                onSecondaryButtonClick = { showDeleteDialog = null }
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