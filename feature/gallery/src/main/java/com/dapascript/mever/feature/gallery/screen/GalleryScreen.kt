package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardType.DOWNLOADED
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.util.getMeverFiles
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.viewmodel.GalleryViewModel

@Composable
internal fun GalleryScreen(
    navigator: BaseNavigator,
    viewModel: GalleryViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val downloadList = downloadList.collectAsStateValue()

    BaseScreen(
        screenName = "Gallery",
        actionMenus = emptyList(),
        onClickBack = { navigator.popBackStack() }
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
                        onShareContentClick = {
                            shareContent(
                                context = context,
                                authority = context.packageName + ".provider",
                                path = getMeverFiles()?.find { file -> file.name == it.fileName }?.path.orEmpty()
                            )
                        },
                        onDeleteContentClick = { deleteDownload(it.id) }
                    )
                )
            }
        } else MeverEmptyItem("You haven't downloaded any files yet")
    }
}