package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_QUICK_NOTIF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingQuickDownloadViewModel @Inject constructor(
    private val repository: MeverRepository,
    subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {

    val isProActive = subscriptionRepository.isProActive

    val isQuickNotifEnabled = repository.getPreference(KEY_QUICK_NOTIF, false).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = false
    )

    fun saveQuickNotifState(isEnabled: Boolean) = viewModelScope.launch {
        repository.savePreference(KEY_QUICK_NOTIF, isEnabled)
    }
}
