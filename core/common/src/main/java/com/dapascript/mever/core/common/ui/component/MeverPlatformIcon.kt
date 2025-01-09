package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.MeverCreamSemiPink
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightGreen
import com.dapascript.mever.core.common.ui.theme.MeverLightPurple
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.util.Constant.PlatformName.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformName.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformName.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformName.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformName.YOUTUBE

@Composable
fun MeverPlatformIcon(
    platform: String,
    iconSize: Dp = Dp40,
    iconPadding: Dp = Dp10,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = getIconBackgroundColor(platform), shape = CircleShape)
            .size(iconSize),
        contentAlignment = Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(iconPadding),
            painter = painterResource(getIcon(platform)),
            contentDescription = "Platform icon"
        )
    }
}

private fun getIcon(platform: String) = when {
    platform.contains(INSTAGRAM) -> R.drawable.ic_instagram
    platform.contains(FACEBOOK) -> R.drawable.ic_facebook
    platform.contains(TWITTER) -> R.drawable.ic_twitter
    platform.contains(YOUTUBE) -> R.drawable.ic_youtube
    platform.contains(TIKTOK) -> R.drawable.ic_tiktok
    else -> -1
}

private fun getIconBackgroundColor(platform: String) = when {
    platform.contains(INSTAGRAM) -> MeverCreamSemiPink
    platform.contains(FACEBOOK) -> MeverLightBlue
    platform.contains(TWITTER) -> MeverLightPurple
    platform.contains(YOUTUBE) -> MeverPink
    platform.contains(TIKTOK) -> MeverLightGreen
    else -> MeverTransparent
}