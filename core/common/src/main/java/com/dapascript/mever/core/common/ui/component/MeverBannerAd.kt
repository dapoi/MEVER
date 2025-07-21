package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.dapascript.mever.core.common.BuildConfig.AD_BANNER_UNIT_ID
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import timber.log.Timber

@SuppressLint("MissingPermission")
@Composable
fun MeverBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = AD_BANNER_UNIT_ID
) {
    if (LocalInspectionMode.current) return

    val context = LocalContext.current
    val adView = remember { AdView(context) }
    LifecycleResumeEffect(adView) {
        adView.resume()
        onPauseOrDispose { adView.pause() }
    }
    DisposableEffect(adView) { onDispose { adView.destroy() } }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            adView.apply {
                this.adUnitId = adUnitId
                val adaptiveAdSize = getAdaptiveAdSize(ctx)
                setAdSize(adaptiveAdSize)

                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Timber.d("Ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        Timber.e("Ad failed to load: ${error.message}")
                    }
                }

                val adRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
        }
    )
}

private fun getAdaptiveAdSize(context: Context): AdSize {
    val displayMetrics = context.resources.displayMetrics
    var adWidthPixels = displayMetrics.widthPixels.toFloat()

    if (adWidthPixels == 0f) {
        adWidthPixels = 320f * displayMetrics.density
    }

    val adWidth = (adWidthPixels / displayMetrics.density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}