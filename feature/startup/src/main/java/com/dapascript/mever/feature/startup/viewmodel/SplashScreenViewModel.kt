package com.dapascript.mever.feature.startup.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : BaseViewModel() {

    private val _isSplashScreenFinished = MutableStateFlow(false)
    val isSplashScreenFinished = _isSplashScreenFinished.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _isSplashScreenFinished.value = true
        }
    }
}