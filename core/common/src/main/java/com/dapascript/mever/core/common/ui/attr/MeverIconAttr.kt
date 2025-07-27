package com.dapascript.mever.core.common.ui.attr

import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamPink
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverLightGreen
import com.dapascript.mever.core.common.ui.theme.MeverLightPurple
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE

object MeverIconAttr {
    fun getPlatformIcon(platform: String) = when {
        platform.contains(INSTAGRAM.platformName) -> R.drawable.ic_instagram
        platform.contains(FACEBOOK.platformName) -> R.drawable.ic_facebook
        platform.contains(TWITTER.platformName) -> R.drawable.ic_twitter
        platform.contains(YOUTUBE.platformName) -> R.drawable.ic_youtube
        platform.contains(TIKTOK.platformName) -> R.drawable.ic_tiktok
        platform.contains(PINTEREST.platformName) -> R.drawable.ic_tiktok
        else -> R.drawable.ic_broken_image
    }

    fun getPlatformIconBackgroundColor(platform: String) = when {
        platform.contains(INSTAGRAM.platformName) -> MeverCreamPink
        platform.contains(FACEBOOK.platformName) -> MeverLightBlue
        platform.contains(TWITTER.platformName) -> MeverLightPurple
        platform.contains(YOUTUBE.platformName) -> MeverPink
        platform.contains(TIKTOK.platformName) -> MeverLightGreen
        platform.contains(PINTEREST.platformName) -> MeverLightGray
        else -> MeverLightGray
    }
}