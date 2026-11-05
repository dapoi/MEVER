package com.dapascript.mever.feature.setting.viewmodel

import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingAboutAppViewModel @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {
    val userId = subscriptionRepository.getAppUserId()
}