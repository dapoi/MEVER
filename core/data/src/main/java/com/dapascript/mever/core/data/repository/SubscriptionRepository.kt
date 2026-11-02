package com.dapascript.mever.core.data.repository

import android.app.Activity
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.flow.StateFlow

data class PurchaseResultData(
    val storeTransaction: StoreTransaction?,
    val customerInfo: CustomerInfo
)

/**
 * Repository for managing RevenueCat subscriptions, customer info,
 * and entitlement checking for `mever_pro`.
 */
interface SubscriptionRepository {
    val isProActive: StateFlow<Boolean>

    fun checkEntitlement()
    suspend fun checkProActive(): Boolean
    suspend fun fetchOfferings(): Result<Offerings>
    suspend fun purchasePackage(activity: Activity, packageToPurchase: Package): Result<PurchaseResultData>
    suspend fun restorePurchases(): Result<CustomerInfo>
}
