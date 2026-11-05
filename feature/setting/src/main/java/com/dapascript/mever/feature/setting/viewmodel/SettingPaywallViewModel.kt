package com.dapascript.mever.feature.setting.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepositoryImpl
import com.dapascript.mever.core.data.repository.SubscriptionRepositoryImpl.RevenueCatPurchaseException
import com.revenuecat.purchases.Package
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingPaywallViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {

    var selectedPackage by mutableStateOf<Package?>(null)
        private set

    var isLoadingOfferings by mutableStateOf(true)
        private set

    var isPurchasing by mutableStateOf(false)
        private set

    var statusMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchOfferings()
    }

    fun fetchOfferings() {
        viewModelScope.launch {
            isLoadingOfferings = true
            statusMessage = null
            val result = subscriptionRepository.fetchOfferings()
            result.onSuccess { offerings ->
                val currentOffering = offerings.current
                selectedPackage = currentOffering?.monthly ?: currentOffering?.availablePackages?.firstOrNull()
                isLoadingOfferings = false
                if (selectedPackage == null) {
                    statusMessage = "No subscription packages currently available."
                }
            }.onFailure { err ->
                isLoadingOfferings = false
                statusMessage = err.message ?: "Failed to load subscription offerings"
            }
        }
    }

    fun purchaseSelectedPackage(
        activity: Activity,
        onSuccess: () -> Unit
    ) {
        val pkg = selectedPackage ?: return
        viewModelScope.launch {
            isPurchasing = true
            statusMessage = null
            val result = subscriptionRepository.purchasePackage(activity, pkg)
            result.onSuccess {
                isPurchasing = false
                onSuccess()
            }.onFailure { err ->
                isPurchasing = false
                statusMessage = if (err is RevenueCatPurchaseException && err.userCancelled) {
                    null
                } else {
                    err.message ?: "Purchase failed"
                }
            }
        }
    }

    fun restorePurchases(
        noActiveSubscriptionMessage: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isPurchasing = true
            statusMessage = null
            val result = subscriptionRepository.restorePurchases()
            result.onSuccess { info ->
                isPurchasing = false
                val isPro = SubscriptionRepositoryImpl.isEntitlementActive(info)
                if (isPro) {
                    onSuccess()
                } else {
                    statusMessage = noActiveSubscriptionMessage
                }
            }.onFailure { err ->
                isPurchasing = false
                statusMessage = err.message ?: "Restore failed"
            }
        }
    }
}