package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.MeverBlueSemiPurple
import com.dapascript.mever.core.common.ui.theme.MeverCreamSemiPink
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverYoungPurple
import com.dapascript.mever.core.common.util.Constant.PlatformName.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformName.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformName.TWITTER

@Composable
fun MeverPlatformIcon(
    platform: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(color = getIconBackgroundColor(platform), shape = CircleShape),
        contentAlignment = Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dp6),
            painter = painterResource(getIcon(platform)),
            contentDescription = "Platform icon"
        )
    }
}

private fun getIcon(platform: String) = when {
    platform.contains(INSTAGRAM) -> R.drawable.ic_instagram
    platform.contains(FACEBOOK) -> R.drawable.ic_facebook
    platform.contains(TWITTER) -> R.drawable.ic_twitter
    else -> R.drawable.ic_tiktok
}

private fun getIconBackgroundColor(platform: String) = when {
    platform.contains(INSTAGRAM) -> MeverCreamSemiPink
    platform.contains(FACEBOOK) -> MeverPink
    platform.contains(TWITTER) -> MeverYoungPurple
    else -> MeverBlueSemiPurple
}