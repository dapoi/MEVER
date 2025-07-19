package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.dapascript.mever.core.common.BuildConfig.AD_BANNER_UNIT_ID
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize.BANNER
import com.google.android.gms.ads.AdView

@SuppressLint("MissingPermission")
@Composable
fun MeverBannerAd(
    context: Context,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) return

    val adView = remember { AdView(context) }
    LifecycleResumeEffect(adView) {
        adView.resume()
        onPauseOrDispose { adView.pause() }
    }
    DisposableEffect(adView) { onDispose { adView.destroy() } }

    AndroidView(
        modifier = modifier,
        factory = {
            adView.apply {
                adUnitId = AD_BANNER_UNIT_ID
                setAdSize(BANNER)

                val adRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
        }
    )
}