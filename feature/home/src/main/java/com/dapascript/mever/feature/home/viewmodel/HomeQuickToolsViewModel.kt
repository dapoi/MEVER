package com.dapascript.mever.feature.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeQuickToolsViewModel @Inject constructor(
    dataStore: MeverDataStore
) : BaseViewModel() {
    val isImageAiEnabled = dataStore.isImageAiEnabled.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = true
    )
    val isGoImgEnabled = dataStore.isGoImgEnabled.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = true
    )
}