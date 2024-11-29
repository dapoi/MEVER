package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import kotlin.Int.Companion.MAX_VALUE
import androidx.compose.ui.text.style.TextAlign.Companion.Center as TextAlignCenter

@Composable
fun MeverEmptyItem(
    description: String,
    modifier: Modifier = Modifier
) {
    val lottieComposition by rememberLottieComposition(RawRes(R.raw.empty_lottie))
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = MAX_VALUE
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Box(modifier = Modifier.size(Dp200)) {
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = lottieComposition,
                contentScale = Crop,
                progress = { progress }
            )
        }
        Spacer(modifier = Modifier.size(Dp8))
        Text(
            text = description,
            textAlign = TextAlignCenter,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}