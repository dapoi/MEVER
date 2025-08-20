package com.dapascript.mever.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {

    val themeType = dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = System
    )

    private val _navigationToHomeEvent = Channel<Unit>()
    val navigationToHomeEvent = _navigationToHomeEvent.receiveAsFlow()

    fun saveUrlIntent(url: String) {
        viewModelScope.launch {
            dataStore.saveUrlIntent(url)
            _navigationToHomeEvent.send(Unit)
        }
    }
}