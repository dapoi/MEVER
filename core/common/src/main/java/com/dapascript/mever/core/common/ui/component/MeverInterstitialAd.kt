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
fun rememberInterstitialAd(onAdComplete: (() -> Unit)? = null): InterstitialAdController {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var adIsLoading by remember { mutableStateOf(false) }
    var isUserTriggered by remember { mutableStateOf(false) }
    var showAd: () -> Unit = {}

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
                    if (isUserTriggered) {
                        isUserTriggered = false
                        showAd()
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
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

    DisposableEffect(Unit) { onDispose { interstitialAd = null } }

    showAd = {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()
                    onAdComplete?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    interstitialAd = null
                    onAdComplete?.invoke()
                }
            }
            interstitialAd?.show(activity)
        } else {
            isUserTriggered = true
            loadAd()
        }
    }

    LaunchedEffect(Unit) { loadAd() }

    return InterstitialAdController { showAd() }
}