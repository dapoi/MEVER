package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.component.MeverPhotoViewer
import com.dapascript.mever.core.common.ui.component.MeverVideoPlayer
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.viewmodel.GalleryPlayerViewModel

@Composable
internal fun GalleryContentDetailScreen(
    navigator: BaseNavigator,
    viewModel: GalleryPlayerViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current

    BaseScreen(
        hideDefaultTopBar = true,
        lockOrientation = false,
        useSystemBarsPadding = false,
        allowScreenOverlap = true,
        statusBarColor = MeverBlack,
        navigationBarColor = MeverBlack,
        modifier = Modifier.background(MeverBlack)
    ) {
        with(args) {
            if (fileName.isVideo()) MeverVideoPlayer(
                source = sourceFile,
                fileName = fileName,
                onClickDelete = { deleteContent(id) },
                onClickShare = {
                    shareContent(
                        context = context,
                        authority = context.packageName,
                        path = sourceFile
                    )
                },
                onClickBack = { navigator.popBackStack() }
            ) else MeverPhotoViewer(
                source = sourceFile,
                fileName = fileName,
                onClickDelete = { deleteContent(id) },
                onClickShare = {
                    shareContent(
                        context = context,
                        authority = context.packageName,
                        path = sourceFile
                    )
                },
                onClickBack = { navigator.popBackStack() }
            )
        }
    }
}