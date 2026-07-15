package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.dapascript.mever.core.common.BuildConfig.AD_BANNER_UNIT_ID
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import timber.log.Timber

@SuppressLint("MissingPermission")
@Composable
fun MeverBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = AD_BANNER_UNIT_ID
) {
    if (LocalInspectionMode.current) return
    val deviceType = LocalDeviceType.current

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { ctx ->
                AdView(ctx).apply {
                    val displayMetrics = ctx.resources.displayMetrics
                    val screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()

                    val adWidth = if (deviceType == PHONE) screenWidthDp else 600
                    val adSize = if (deviceType == PHONE) {
                        AdSize.getLargeAnchoredAdaptiveBannerAdSize(ctx, screenWidthDp)
                    } else {
                        AdSize.getInlineAdaptiveBannerAdSize(adWidth, 64)
                    }

                    val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()

                    loadAd(adRequest, object : AdLoadCallback<BannerAd> {
                        override fun onAdLoaded(ad: BannerAd) {
                            Timber.d("Ad loaded successfully")
                            ad.adEventCallback = object : AdEventCallback, BannerAdEventCallback {
                                override fun onAdImpression() {
                                    Timber.d("Ad impression recorded")
                                }

                                override fun onAdClicked() {
                                    Timber.d("Ad clicked")
                                }
                            }
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Timber.e("Ad failed to load: ${adError.message}")
                        }
                    })
                }
            },
            onRelease = { adView ->
                adView.destroy()
                Timber.d("AdView destroyed")
            }
        )
    }
}