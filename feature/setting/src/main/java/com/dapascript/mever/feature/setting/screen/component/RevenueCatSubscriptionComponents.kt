package com.dapascript.mever.feature.setting.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.models.StoreTransaction
import com.revenuecat.purchases.ui.revenuecatui.customercenter.CustomerCenter
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialog
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialogOptions
import com.revenuecat.purchases.ui.revenuecatui.PaywallListener

@Composable
fun RevenueCatPaywallDialog(
    shouldDisplay: Boolean,
    onDismiss: () -> Unit,
    onPurchaseSuccess: (CustomerInfo) -> Unit = {}
) {
    if (shouldDisplay) {
        PaywallDialog(
            paywallDialogOptions = PaywallDialogOptions.Builder()
                .setDismissRequest(onDismiss)
                .setListener(object : PaywallListener {
                    override fun onPurchaseCompleted(
                        customerInfo: CustomerInfo,
                        storeTransaction: StoreTransaction
                    ) {
                        onPurchaseSuccess(customerInfo)
                        onDismiss()
                    }

                    override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                        onPurchaseSuccess(customerInfo)
                        onDismiss()
                    }
                })
                .build()
        )
    }
}

@Composable
fun RevenueCatCustomerCenterDialog(
    shouldDisplay: Boolean,
    onDismiss: () -> Unit
) {
    if (shouldDisplay) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.whiteDark),
                color = colors.whiteDark
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .background(colors.whiteDark)
                ) {
                    CustomerCenter(
                        onDismiss = onDismiss
                    )
                }
            }
        }
    }
}