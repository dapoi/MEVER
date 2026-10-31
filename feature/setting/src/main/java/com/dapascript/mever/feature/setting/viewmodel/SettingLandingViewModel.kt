package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.getAppVersion
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_PIP
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_QUICK_NOTIF
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
internal class SettingLandingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MeverRepository,
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {
    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading = _isInitialLoading.asStateFlow()

    val isProActive = subscriptionRepository.isProActive

    var languageCode by mutableStateOf("en")
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

    val isQuickNotifEnabled = repository.getPreference(KEY_QUICK_NOTIF, false).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = false
    )

    init {
        viewModelScope.launch {
            val version = withContext(IO) { getAppVersion(context) }
            appVersion = version
            delay(200.milliseconds)
            _isInitialLoading.value = false
        }
    }

    fun savePipState(isPipEnabled: Boolean) = viewModelScope.launch {
        repository.savePreference(KEY_PIP, isPipEnabled)
    }

    fun saveQuickNotifState(isEnabled: Boolean) = viewModelScope.launch {
        repository.savePreference(KEY_QUICK_NOTIF, isEnabled)
    }

    fun checkSubscriptionEntitlement() {
        subscriptionRepository.checkEntitlement()
    }
}
