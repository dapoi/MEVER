package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.component.MeverPhotoViewer
import com.dapascript.mever.core.common.ui.component.MeverVideoPlayer
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.viewmodel.GalleryPlayerViewModel
import java.io.File

@Composable
internal fun GalleryContentDetailScreen(
    navigator: NavController,
    viewModel: GalleryPlayerViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current

    BaseScreen(
        useSystemBarsPadding = false,
        allowScreenOverlap = true,
        hideDefaultTopBar = true,
        lockOrientation = false
    ) {
        with(args) {
            if (isVideo(filePath)) MeverVideoPlayer(
                modifier = Modifier.fillMaxSize(),
                source = filePath,
                onClickDelete = { deleteContent(id) },
                onClickShare = {
                    shareContent(
                        context = context,
                        file = File(filePath)
                    )
                },
                onClickBack = { navigator.popBackStack() }
            ) else MeverPhotoViewer(
                modifier = Modifier.fillMaxSize(),
                source = filePath,
                onClickDelete = { deleteContent(id) },
                onClickShare = {
                    shareContent(
                        context = context,
                        file = File(filePath)
                    )
                },
                onClickBack = { navigator.popBackStack() }
            )
        }
    }
}