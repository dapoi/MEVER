package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_PIP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryContentDetailViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

    val isPipEnabled = repository.getPreference(KEY_PIP, true).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    val getButtonClickCount = repository.getClickCount().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 0
    )

    val adsThreshold = repository.getAdsThreshold().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 3
    )

    fun incrementClickCount() = viewModelScope.launch {
        repository.incrementClickCount()
    }
}