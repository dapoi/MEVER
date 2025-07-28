package com.dapascript.mever.core.common.ui.attr

import androidx.compose.runtime.State

object MeverInterstitialAdAttr {
    data class InterstitialAdController(
        val isAdReady: State<Boolean>,
        val showAd: () -> Unit
    )
}