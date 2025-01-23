package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.feature.gallery.navigation.route.GalleryContentDetailRoute
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryPlayerViewModel @Inject constructor(
    private val ketch: Ketch,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val galleryContentDetailRoute = savedStateHandle.toRoute<GalleryContentDetailRoute>()

    fun deleteContent(id: Int) = viewModelScope.launch {
        ketch.clearDb(id)
    }
}