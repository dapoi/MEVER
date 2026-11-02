package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class SettingSubscriptionDetailViewModel @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {

    val isProActive = subscriptionRepository.isProActive
    val proWillRenew = subscriptionRepository.proWillRenew
    val expirationDateString = subscriptionRepository.proExpirationDate.map { date ->
        date?.let {
            SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = null
    )
}