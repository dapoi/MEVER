package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLandingViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    val settingMenus by lazy { getSettingMenus() }
    var titleHeight by mutableIntStateOf(0)

    private val _getLanguageCode = MutableStateFlow("en")
    val getLanguageCode = _getLanguageCode.asStateFlow()

    private val _themeType = MutableStateFlow(System)
    val themeType = _themeType.asStateFlow()

    val isPipEnabled = dataStore.isPipEnabled.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    init {
        viewModelScope.launch {
            combine(
                dataStore.getLanguageCode,
                dataStore.getTheme
            ) { language, theme ->
                _getLanguageCode.value = language
                _themeType.value = theme
            }.collect()
        }
    }

    fun savePipState(isPipEnabled: Boolean) = viewModelScope.launch {
        dataStore.setPipEnabled(isPipEnabled)
    }
}