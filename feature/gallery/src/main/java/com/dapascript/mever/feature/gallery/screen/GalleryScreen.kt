package com.dapascript.mever.feature.gallery.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.feature.gallery.viewmodel.GalleryViewModel

@Composable
internal fun GalleryScreen(
    navigator: BaseNavigator,
    viewModel: GalleryViewModel = hiltViewModel()
) = with(viewModel) {

    BaseScreen(
        screenName = "Gallery",
        actionMenus = emptyList(),
        onClickBack = { navigator.popBackStack() }
    ) {
        LazyColumn{

        }
    }
}