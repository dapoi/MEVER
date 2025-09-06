package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.storage.StorageUtil.getStorageInfo
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLandingViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    val settingMenus by lazy { getSettingMenus() }
    val storageInfo by lazy { getStorageInfo(context) }

    var titleHeight by mutableIntStateOf(0)
    var getLanguageCode by mutableStateOf("en")
    var animatedPercent by mutableFloatStateOf(0f)

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