package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.DpHalf
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverCreamSemiPink
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverYoungPurple
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN

@Composable
fun MeverPlatformIcon(
    type: PlatformType,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .background(color = getIconDetail(type)[0] as Color, shape = CircleShape)
        .border(width = DpHalf, color = MeverBlack.copy(alpha = 0.2f), shape = CircleShape),
    contentAlignment = Center
) {
    Image(
        painter = painterResource(getIconDetail(type)[1] as Int),
        contentDescription = "Platform Icon",
        contentScale = Fit,
        modifier = Modifier.padding(Dp12)
    )
}

@Composable
private fun getIconDetail(type: PlatformType) = when (type) {
    FACEBOOK -> listOf(MeverPink, R.drawable.ic_facebook)
    INSTAGRAM -> listOf(MeverCreamSemiPink, R.drawable.ic_instagram)
    TWITTER -> listOf(MeverYoungPurple, R.drawable.ic_twitter)
    UNKNOWN -> listOf()
}