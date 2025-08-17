package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryPlayerViewModel @Inject constructor(
    private val ketch: Ketch,
    dataStore: MeverDataStore,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val args by lazy { GalleryContentDetailRoute.getArgs(savedStateHandle) }

    val themeType = dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = System
    )

    val isPipEnabled = dataStore.isPipEnabled.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    fun deleteContent(id: Int) = viewModelScope.launch {
        ketch.clearDb(id)
    }
}