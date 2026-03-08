package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.navigation.helper.getArgs
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingThemeRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingThemeViewModel @Inject constructor(
    private val dataStore: MeverDataStore,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val args by lazy { savedStateHandle.getArgs<SettingThemeRoute>() }
    var titleHeight by mutableIntStateOf(0)

    fun setThemeType(mode: ThemeType) = viewModelScope.launch {
        dataStore.saveTheme(mode)
    }
}