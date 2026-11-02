package com.dapascript.mever.core.data.repository

import android.app.Activity
import com.revenuecat.purchases.CacheFetchPolicy
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.PurchasesException
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import com.revenuecat.purchases.purchaseWith
import com.revenuecat.purchases.restorePurchasesWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class SubscriptionRepositoryImpl @Inject constructor() : SubscriptionRepository {

    private val _isProActive = MutableStateFlow(false)
    override val isProActive = _isProActive.asStateFlow()

    private val _isEntitlementChecked = MutableStateFlow(false)
    override val isEntitlementChecked = _isEntitlementChecked.asStateFlow()

    init {
        setupCustomerInfoListener()
        checkEntitlement()
    }

    private fun setupCustomerInfoListener() {
        try {
            if (Purchases.isConfigured) {
                Purchases.sharedInstance.updatedCustomerInfoListener =
                    UpdatedCustomerInfoListener { info ->
                        updateState(info)
                    }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to setup RevenueCat UpdatedCustomerInfoListener")
        }
    }

    override fun getAppUserId() = if (Purchases.isConfigured) {
        Purchases.sharedInstance.appUserID
    } else ""

    override fun checkEntitlement() {
        if (!Purchases.isConfigured) return
        setupCustomerInfoListener()
        // Fast-path: check local cache first so entitlement is restored immediately without waiting for network
        Purchases.sharedInstance.getCustomerInfoWith(
            fetchPolicy = CacheFetchPolicy.CACHE_ONLY,
            onError = { /* Ignore cache miss, network call will follow */ },
            onSuccess = { info ->
                updateState(info)
            }
        )
        Purchases.sharedInstance.getCustomerInfoWith(
            fetchPolicy = CacheFetchPolicy.FETCH_CURRENT,
            onError = { error ->
                Timber.e("Error fetching customer info: ${error.message}")
            },
            onSuccess = { info ->
                updateState(info)
            }
        )
    }

    override suspend fun checkProActive(): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (!Purchases.isConfigured) {
                if (continuation.isActive) continuation.resume(_isProActive.value)
                return@suspendCancellableCoroutine
            }
            setupCustomerInfoListener()
            Purchases.sharedInstance.getCustomerInfoWith(
                fetchPolicy = CacheFetchPolicy.FETCH_CURRENT,
                onError = { error ->
                    Timber.e("Error fetching customer info in checkProActive: ${error.message}")
                    if (continuation.isActive) continuation.resume(_isProActive.value)
                },
                onSuccess = { info ->
                    updateState(info)
                    if (continuation.isActive) continuation.resume(_isProActive.value)
                }
            )
        }

    override suspend fun fetchOfferings(): Result<Offerings> =
        suspendCancellableCoroutine { continuation ->
            if (!Purchases.isConfigured) {
                if (continuation.isActive) {
                    continuation.resume(Result.failure(IllegalStateException("RevenueCat is not configured")))
                }
                return@suspendCancellableCoroutine
            }
            Purchases.sharedInstance.getOfferingsWith(
                onError = { error ->
                    Timber.e("Error in fetchOfferings: ${error.message}")
                    if (continuation.isActive) {
                        continuation.resume(Result.failure(PurchasesException(error)))
                    }
                },
                onSuccess = { offerings ->
                    if (continuation.isActive) {
                        continuation.resume(Result.success(offerings))
                    }
                }
            )
        }

    override suspend fun purchasePackage(
        activity: Activity,
        packageToPurchase: Package
    ): Result<PurchaseResultData> = suspendCancellableCoroutine { continuation ->
        if (!Purchases.isConfigured) {
            if (continuation.isActive) {
                continuation.resume(Result.failure(IllegalStateException("RevenueCat is not configured")))
            }
            return@suspendCancellableCoroutine
        }
        setupCustomerInfoListener()
        val params = PurchaseParams.Builder(activity, packageToPurchase).build()
        Purchases.sharedInstance.purchaseWith(
            purchaseParams = params,
            onError = { error, userCancelled ->
                Timber.e("Purchase error (cancelled=$userCancelled): ${error.message}")
                if (continuation.isActive) {
                    continuation.resume(
                        Result.failure(
                            RevenueCatPurchaseException(
                                error,
                                userCancelled
                            )
                        )
                    )
                }
            },
            onSuccess = { storeTransaction, info ->
                updateState(info)
                if (continuation.isActive) {
                    continuation.resume(Result.success(PurchaseResultData(storeTransaction, info)))
                }
            }
        )
    }

    override suspend fun restorePurchases(): Result<CustomerInfo> =
        suspendCancellableCoroutine { continuation ->
            if (!Purchases.isConfigured) {
                if (continuation.isActive) {
                    continuation.resume(Result.failure(IllegalStateException("RevenueCat is not configured")))
                }
                return@suspendCancellableCoroutine
            }
            setupCustomerInfoListener()
            Purchases.sharedInstance.restorePurchasesWith(
                onError = { error ->
                    Timber.e("Restore error: ${error.message}")
                    if (continuation.isActive) {
                        continuation.resume(Result.failure(PurchasesException(error)))
                    }
                },
                onSuccess = { info ->
                    updateState(info)
                    if (continuation.isActive) {
                        continuation.resume(Result.success(info))
                    }
                }
            )
        }

    private fun updateState(info: CustomerInfo) {
        val isPro = isEntitlementActive(info)
        _isProActive.value = isPro
        _isEntitlementChecked.value = true
        Timber.d("Updated customer info. pro_access active: $isPro")
    }

    class RevenueCatPurchaseException(
        val error: PurchasesError,
        val userCancelled: Boolean = false
    ) : Exception(error.message)

    companion object {
        const val MEVER_PRO = "mever_pro"

        fun isEntitlementActive(info: CustomerInfo): Boolean {
            return info.entitlements[MEVER_PRO]?.isActive == true || info.entitlements.active.isNotEmpty()
        }
    }
}