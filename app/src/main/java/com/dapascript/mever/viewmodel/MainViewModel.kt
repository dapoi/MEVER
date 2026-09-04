package com.dapascript.mever.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.BuildConfig.VERSION_NAME
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_URL_INTENT
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_VERSION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

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

    private val _navigationToHomeEvent = Channel<Unit>()
    val navigationToHomeEvent = _navigationToHomeEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.savePreference(KEY_VERSION, VERSION_NAME)
        }
    }

    fun saveUrlIntent(url: String) {
        viewModelScope.launch {
            repository.savePreference(KEY_URL_INTENT, url)
            _navigationToHomeEvent.send(Unit)
        }
    }
}