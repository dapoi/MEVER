package com.dapascript.mever.feature.startup.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val meverDataStore: MeverDataStore
) : BaseViewModel() {

    fun setIsOnboarded(isOnboarded: Boolean) = viewModelScope.launch {
        meverDataStore.setIsOnboarded(isOnboarded)
    }
}