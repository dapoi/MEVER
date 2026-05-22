package com.dapascript.mever.core.common.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.BuildConfig.AD_INTERSTITIAL_UNIT_ID
import com.dapascript.mever.core.common.ui.attr.MeverInterstitialAdAttr.InterstitialAdController
import com.dapascript.mever.core.common.util.LocalActivity
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback

@Composable
fun rememberInterstitialAd(onAdComplete: (() -> Unit)? = null): InterstitialAdController {
    val activity = LocalActivity.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var adIsLoading by remember { mutableStateOf(false) }
    var isUserTriggered by remember { mutableStateOf(false) }
    var showAd: () -> Unit = {}

    fun loadAd() {
        if (adIsLoading || interstitialAd != null) return
        adIsLoading = true

        val adRequest = AdRequest.Builder(AD_INTERSTITIAL_UNIT_ID).build()
        InterstitialAd.load(
            adRequest = adRequest,
            adLoadCallback = object : InterstitialAdEventCallback, AdLoadCallback<InterstitialAd> {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    adIsLoading = false

                    ad.adEventCallback = object : InterstitialAdEventCallback {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            loadAd()
                            onAdComplete?.invoke()
                        }

                        override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                            interstitialAd = null
                            onAdComplete?.invoke()
                        }
                    }

                    if (isUserTriggered) {
                        isUserTriggered = false
                        showAd()
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    adIsLoading = false
                    if (isUserTriggered) {
                        onAdComplete?.invoke()
                        isUserTriggered = false
                    }
                }
            }
        )
    }

    showAd = {
        interstitialAd?.show(activity) ?: run {
            isUserTriggered = true
            loadAd()
        }
    }

    LaunchedEffect(Unit) { loadAd() }
    DisposableEffect(Unit) { onDispose { interstitialAd = null } }

    return InterstitialAdController { showAd() }
}