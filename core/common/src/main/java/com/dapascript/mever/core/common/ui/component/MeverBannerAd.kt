package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dapascript.mever.core.common.BuildConfig.AD_BANNER_UNIT_ID
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRefreshCallback
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import timber.log.Timber

@Composable
fun MeverBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = AD_BANNER_UNIT_ID
) {
    if (LocalInspectionMode.current) return
    val deviceType = LocalDeviceType.current
    val activity = LocalActivity.current
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp().value.toInt() }
    val adHeight = if (deviceType == PHONE) 60 else 90

    Box(modifier = modifier.height(adHeight.dp)) {
        key(adUnitId, deviceType, screenWidth) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(adHeight.dp),
                factory = {
                    AdView(activity).apply {
                        val adSize = AdSize.getInlineAdaptiveBannerAdSize(
                            width = screenWidth,
                            maxHeight = adHeight
                        )

                        val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()

                        loadAd(adRequest, object : AdLoadCallback<BannerAd> {
                            override fun onAdLoaded(ad: BannerAd) {
                                Timber.d("Banner ad loaded successfully")
                                ad.bannerAdRefreshCallback = object : BannerAdRefreshCallback {
                                    override fun onAdRefreshed() {
                                        Timber.d("Banner ad refreshed")
                                    }

                                    override fun onAdFailedToRefresh(adError: LoadAdError) {
                                        Timber.e("Banner ad failed to refresh: ${adError.code} - ${adError.message}")
                                    }
                                }
                                ad.adEventCallback = object : BannerAdEventCallback {
                                    override fun onAdImpression() {
                                        Timber.d("Banner ad impression recorded")
                                    }

                                    override fun onAdClicked() {
                                        Timber.d("Banner ad clicked")
                                    }

                                    override fun onAdFailedToShowFullScreenContent(
                                        fullScreenContentError: FullScreenContentError
                                    ) {
                                        Timber.e("Banner ad failed to show full screen content: ${fullScreenContentError.message}")
                                    }
                                }
                            }

                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Timber.e("Banner ad failed to load: ${adError.code} - ${adError.message}")
                            }
                        })
                    }
                },
                onRelease = { adView ->
                    adView.destroy()
                    Timber.d("Banner ad view destroyed")
                }
            )
        }
    }
}