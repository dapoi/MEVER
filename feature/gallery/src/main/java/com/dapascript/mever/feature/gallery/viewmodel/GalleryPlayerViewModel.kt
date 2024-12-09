package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.feature.gallery.navigation.route.GalleryContentViewerRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val galleryContentViewerRoute = savedStateHandle.toRoute<GalleryContentViewerRoute>()
}