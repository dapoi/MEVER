package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
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

@Composable
fun MeverBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = AD_BANNER_UNIT_ID
) {
    if (LocalInspectionMode.current) return
    val deviceType = LocalDeviceType.current
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidthDp = with(density) { windowInfo.containerSize.width.toDp().value.toInt() }

    Box(modifier = modifier) {
        key(adUnitId, deviceType, screenWidthDp) {
            AndroidView(
                modifier = Modifier.wrapContentSize(),
                factory = { ctx ->
                    AdView(ctx).apply {
                        val adHeight = if (deviceType == PHONE) 60 else 90
                        val adSize = AdSize.getInlineAdaptiveBannerAdSize(screenWidthDp, adHeight)

                        val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()

                        loadAd(adRequest, object : AdLoadCallback<BannerAd> {
                            override fun onAdLoaded(ad: BannerAd) {
                                Timber.d("Ad loaded successfully")
                                ad.adEventCallback =
                                    object : AdEventCallback, BannerAdEventCallback {
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
}