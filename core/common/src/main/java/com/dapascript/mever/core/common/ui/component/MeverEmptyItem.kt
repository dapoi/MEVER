package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dapascript.mever.core.common.R
import kotlin.Int.Companion.MAX_VALUE

@Composable
fun MeverEmptyItem(modifier: Modifier = Modifier) {
    val lottieComposition by rememberLottieComposition(RawRes(R.raw.empty_lottie))
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = MAX_VALUE
    )

    LottieAnimation(
        modifier = modifier.fillMaxSize(),
        composition = lottieComposition,
        progress = { progress }
    )
}