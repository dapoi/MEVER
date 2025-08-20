package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLandingViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    val settingMenus by lazy { getSettingMenus() }

    var titleHeight by mutableIntStateOf(0)
    var getLanguageCode by mutableStateOf("")

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

    fun savePipState(isPipEnabled: Boolean) = viewModelScope.launch {
        dataStore.setPipEnabled(isPipEnabled)
    }
}