package com.dapascript.mever.core.common.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.dapascript.mever.core.common.BuildConfig.AD_INTERSTITIAL_UNIT_ID
import com.dapascript.mever.core.common.ui.attr.MeverInterstitialAdAttr.InterstitialAdController
import com.dapascript.mever.core.common.util.LocalActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun rememberInterstitialAd(
    onAdFailToLoad: (() -> Unit)? = null,
    onAdFailOrDismissed: (() -> Unit)? = null
): InterstitialAdController {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var adIsLoading by remember { mutableStateOf(false) }
    val isAdReadyState = remember { mutableStateOf(false) }
    var isUserTriggered by remember { mutableStateOf(false) }

    fun loadAd() {
        if (adIsLoading || interstitialAd != null) return
        adIsLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AD_INTERSTITIAL_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    adIsLoading = false
                    isAdReadyState.value = true
                    isUserTriggered = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    adIsLoading = false
                    isAdReadyState.value = false
                    if (isUserTriggered) {
                        onAdFailToLoad?.invoke()
                        isUserTriggered = false
                    }
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            interstitialAd = null
            isAdReadyState.value = false
        }
    }

    val showAd = {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    isAdReadyState.value = false
                    loadAd()
                    onAdFailOrDismissed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    interstitialAd = null
                    isAdReadyState.value = false
                    onAdFailOrDismissed?.invoke()
                }
            }
            interstitialAd?.show(activity)
        } else {
            isUserTriggered = true
            loadAd()
        }
    }

    LaunchedEffect(Unit) { loadAd() }

    return InterstitialAdController(isAdReadyState) { showAd() }
}