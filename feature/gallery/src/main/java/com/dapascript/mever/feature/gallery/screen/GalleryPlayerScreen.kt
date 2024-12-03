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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.base.attr.BaseScreenAttr.BaseScreenArgs
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.component.MeverVideoPlayer
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.feature.gallery.viewmodel.GalleryPlayerViewModel

@Composable
internal fun GalleryPlayerScreen(
    navigator: BaseNavigator,
    viewModel: GalleryPlayerViewModel = hiltViewModel()
) = with(viewModel) {
    var orientation by remember { mutableIntStateOf(ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current

    BaseScreen(
        baseScreenArgs = BaseScreenArgs(
            screenName = "",
            statusBarColor = MeverBlack,
            navigationBarColor = MeverBlack,
            colorBackIcon = MeverWhite,
            topBarColor = MeverBlack,
            onClickBack = { navigator.popBackStack() }
        ),
        isLockOrientation = false,
        isUseSystemBarsPadding = false,
        overlappingTopBar = true,
        hideTopBar = true,
        modifier = Modifier.background(MeverBlack)
    ) {
        LaunchedEffect(configuration) {
            snapshotFlow { configuration.orientation }.collect { orientation = it }
        }

        with(galleryPlayerRoute) {
            MeverVideoPlayer(
                sourceVideo = sourceVideo,
                fileName = fileName
            ) { navigator.popBackStack() }
        }
    }
}