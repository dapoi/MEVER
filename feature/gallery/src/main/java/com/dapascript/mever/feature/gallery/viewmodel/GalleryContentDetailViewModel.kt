package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryContentDetailViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {

    val isPipEnabled = dataStore.isPipEnabled.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    val getButtonClickCount = dataStore.clickCount.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 0
    )

    val adsThreshold = dataStore.adsThreshold.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 3
    )

    fun incrementClickCount() = viewModelScope.launch {
        dataStore.incrementClickCount()
    }
}