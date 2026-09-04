package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.getAppVersion
import com.dapascript.mever.core.common.util.storage.StorageUtil.StorageInfo
import com.dapascript.mever.core.common.util.storage.StorageUtil.getStorageInfo
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_PIP
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class SettingLandingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MeverRepository
) : BaseViewModel() {
    var languageCode by mutableStateOf("en")
    var animatedPercent by mutableFloatStateOf(0f)
    var appVersion by mutableStateOf("")

    val themeType = repository.getPreference(KEY_THEME, System.name).map { name ->
        try {
            ThemeType.valueOf(name)
        } catch (_: IllegalArgumentException) {
            System
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = System
    )

    val isPipEnabled = repository.getPreference(KEY_PIP, true).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    private val _storageInfo = MutableStateFlow<StorageInfo?>(null)
    val storageInfo = _storageInfo.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            val version = getAppVersion(context)
            withContext(Main) {
                appVersion = version
            }
        }
    }

    fun fetchStorageInfo() = viewModelScope.launch {
        val storageInfo = withContext(IO) { getStorageInfo(context) }
        _storageInfo.value = storageInfo
    }

    fun savePipState(isPipEnabled: Boolean) = viewModelScope.launch {
        repository.savePreference(KEY_PIP, isPipEnabled)
    }
}