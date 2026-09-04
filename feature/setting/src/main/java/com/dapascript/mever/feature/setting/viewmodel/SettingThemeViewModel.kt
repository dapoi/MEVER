package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingThemeViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {
    fun setThemeType(mode: ThemeType) = viewModelScope.launch {
        repository.savePreference(KEY_THEME, mode.name)
    }
}