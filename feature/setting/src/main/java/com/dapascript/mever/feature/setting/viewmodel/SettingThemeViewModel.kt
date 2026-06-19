package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingThemeViewModel @Inject constructor(
    private val dataStore: MeverDataStore,
) : BaseViewModel() {
    fun setThemeType(mode: ThemeType) = viewModelScope.launch {
        dataStore.saveTheme(mode)
    }
}