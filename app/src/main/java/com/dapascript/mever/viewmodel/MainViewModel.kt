package com.dapascript.mever.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.BuildConfig.VERSION_NAME
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.PATH_HOME
import com.dapascript.mever.core.data.deeplink.DeeplinkManager
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_LINK_CONTENT
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
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
    private val repository: MeverRepository,
    private val deeplinkManager: DeeplinkManager
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

    private val _navigationEvent = Channel<String>(capacity = Channel.CONFLATED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.savePreference(KEY_VERSION, VERSION_NAME)
        }
    }

    fun saveLinkContent(link: String, isTriggerNavigation: Boolean = true) {
        viewModelScope.launch {
            Log.d("MainViewModel", "Saving link content: $link, triggerNav: $isTriggerNavigation")
            repository.savePreference(KEY_LINK_CONTENT, link)
            if (isTriggerNavigation) {
                Log.d("MainViewModel", "Sending navigation event: $PATH_HOME")
                _navigationEvent.send(PATH_HOME)
            }
        }
    }

    fun handleDeeplink(uri: Uri, isTriggerNavigation: Boolean = true) {
        viewModelScope.launch {
            Log.d("MainViewModel", "Handling deeplink: $uri, triggerNav: $isTriggerNavigation")
            deeplinkManager.handleDeeplink(uri)
            if (isTriggerNavigation) {
                val path = uri.path.orEmpty()
                Log.d("MainViewModel", "Sending navigation event: $path")
                _navigationEvent.send(path)
            }
        }
    }
}