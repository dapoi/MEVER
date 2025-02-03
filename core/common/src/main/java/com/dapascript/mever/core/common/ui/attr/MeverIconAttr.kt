package com.dapascript.mever.core.common.ui.attr

import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamPink
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

object MeverIconAttr {
    fun getPlatformIcon(platform: String) = when {
        platform.contains(INSTAGRAM) -> R.drawable.ic_instagram
        platform.contains(FACEBOOK) -> R.drawable.ic_facebook
        platform.contains(TWITTER) -> R.drawable.ic_twitter
        platform.contains(YOUTUBE) -> R.drawable.ic_youtube
        platform.contains(TIKTOK) -> R.drawable.ic_tiktok
        else -> -1
    }

    fun getPlatformIconBackgroundColor(platform: String) = when {
        platform.contains(INSTAGRAM) -> MeverCreamPink
        platform.contains(FACEBOOK) -> MeverLightBlue
        platform.contains(TWITTER) -> MeverLightPurple
        platform.contains(YOUTUBE) -> MeverPink
        platform.contains(TIKTOK) -> MeverLightGreen
        else -> MeverTransparent
    }
}