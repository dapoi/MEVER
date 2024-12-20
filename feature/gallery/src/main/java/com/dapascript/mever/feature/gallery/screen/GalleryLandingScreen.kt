package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.navigation.route.GalleryPlayerRoute
import com.dapascript.mever.feature.gallery.viewmodel.GalleryLandingViewModel

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

        if (downloadList.isNotEmpty()) LazyColumn {
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
                        fileName = it.fileName,
                        status = it.status,
                        progress = it.progress,
                        total = it.total,
                        path = it.path,
                        type = DOWNLOADED,
                        onPlayClick = {
                            navigator.navigate(
                                GalleryPlayerRoute(
                                    sourceVideo = getMeverFiles()?.find { file ->
                                        file.name == it.fileName
                                    }?.path.orEmpty(),
                                    fileName = it.fileName
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
        } else MeverEmptyItem("You haven't downloaded any files yet")
    }

    showDeleteDialog?.let { id ->
        MeverDialog(
            showDialog = true,
            meverDialogArgs = MeverDialogArgs(
                title = "Delete this file?",
                actionText = "Delete",
                onActionClick = {
                    deleteDownload(id)
                    showDeleteDialog = null
                },
                onDismissClick = { showDeleteDialog = null }
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