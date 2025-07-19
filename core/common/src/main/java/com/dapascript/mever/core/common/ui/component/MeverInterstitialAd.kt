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
import com.dapascript.mever.core.common.util.LocalActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun rememberInterstitialAd(): () -> Unit {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var adIsLoading by remember { mutableStateOf(false) }

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
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    adIsLoading = false
                }
            }
        )
    }

    LaunchedEffect(Unit) { loadAd() }

    DisposableEffect(Unit) {
        onDispose { interstitialAd = null }
    }

    return {
        if (setOf(interstitialAd, activity).any { it != null }) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    interstitialAd = null
                }
            }
            interstitialAd?.show(activity)
        } else loadAd()
    }
}