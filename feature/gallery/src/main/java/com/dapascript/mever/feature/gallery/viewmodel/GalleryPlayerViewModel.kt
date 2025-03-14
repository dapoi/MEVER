package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryPlayerViewModel @Inject constructor(
    private val ketch: Ketch,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val args by lazy { savedStateHandle.toRoute<GalleryContentDetailRoute>() }

    fun deleteContent(id: Int) = viewModelScope.launch {
        ketch.clearDb(id)
    }
}