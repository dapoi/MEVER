package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingAboutAppViewModel @Inject constructor(
    dataStore: MeverDataStore
) : BaseViewModel() {
    val themeType =dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = System
    )
}