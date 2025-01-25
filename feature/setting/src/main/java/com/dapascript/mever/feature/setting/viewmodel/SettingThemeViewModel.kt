package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingThemeViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    var titleHeight by mutableIntStateOf(0)

    private val _themeType = MutableStateFlow(System)
    val themeType = _themeType.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            dataStore.getTheme.collect { _themeType.value = it }
        }
    }

    fun setThemeType(mode: ThemeType) = viewModelScope.launch {
        dataStore.saveTheme(mode)
    }
}