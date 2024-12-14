package com.dapascript.mever.feature.startup.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    meverDataStore: MeverDataStore
) : BaseViewModel() {

    private val _isSplashScreenFinished = MutableStateFlow(false)
    val isSplashScreenFinished = _isSplashScreenFinished.asStateFlow()

    val isOnboarded = meverDataStore.isOnboarded.map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    init {
        viewModelScope.launch {
            delay(2000)
            _isSplashScreenFinished.value = true
        }
    }
}