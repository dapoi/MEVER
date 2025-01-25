package com.dapascript.mever

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: MeverDataStore
) : BaseViewModel() {
    private val _themeType = MutableStateFlow(System)
    val themeType = _themeType.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.getTheme.collect { _themeType.value = it }
        }
    }
}