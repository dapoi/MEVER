package com.dapascript.mever.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.BuildConfig.VERSION_NAME
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.PATH_HOME
import com.dapascript.mever.core.data.deeplink.DeeplinkManager
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_LAST_INTERACTION_TIME
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_LINK_CONTENT
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_VERSION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.first
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

    val themeType = repository.getPreference(KEY_THEME, ThemeType.System.name).map { name ->
        try {
            ThemeType.valueOf(name)
        } catch (_: IllegalArgumentException) {
            ThemeType.System
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = ThemeType.System
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
            repository.savePreference(KEY_LINK_CONTENT, link)
            if (isTriggerNavigation) {
                _navigationEvent.send(PATH_HOME)
            }
        }
    }

    fun handleDeeplink(uri: Uri, isTriggerNavigation: Boolean = true) {
        viewModelScope.launch {
            deeplinkManager.handleDeeplink(uri)
            if (isTriggerNavigation) {
                val path = uri.path.orEmpty()
                _navigationEvent.send(path)
            }
        }
    }

    fun updateInteractionTime() {
        viewModelScope.launch {
            repository.savePreference(KEY_LAST_INTERACTION_TIME, System.currentTimeMillis())
        }
    }

    fun checkIdleTimeout(onTimeout: () -> Unit) {
        viewModelScope.launch {
            val lastTime = repository.getPreference(KEY_LAST_INTERACTION_TIME, 0L).first()
            val threeMinutes = 3 * 60 * 1000L
            val currentTime = System.currentTimeMillis()
            if (lastTime != 0L && (currentTime - lastTime) > threeMinutes) {
                onTimeout()
            }
            updateInteractionTime()
        }
    }
}