package com.dapascript.mever.feature.gallery.screen

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.component.MeverPhotoViewer
import com.dapascript.mever.core.common.ui.component.MeverVideoViewer
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.core.common.util.shareContent
import com.dapascript.mever.feature.gallery.viewmodel.GalleryPlayerViewModel

@Composable
internal fun GalleryContentViewerScreen(
    navigator: BaseNavigator,
    viewModel: GalleryPlayerViewModel = hiltViewModel()
) = with(viewModel) {
    var orientation by remember { mutableIntStateOf(ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
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
        LaunchedEffect(configuration) {
            snapshotFlow { configuration.orientation }.collect { orientation = it }
        }

        with(galleryContentViewerRoute) {
            if (fileName.isVideo()) MeverVideoViewer(
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