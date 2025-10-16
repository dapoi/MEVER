package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamPink
import com.dapascript.mever.core.common.ui.theme.MeverGreen
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverLightGreen
import com.dapascript.mever.core.common.ui.theme.MeverLightPink
import com.dapascript.mever.core.common.ui.theme.MeverLightPurple
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverSoftGray
import com.dapascript.mever.core.common.ui.theme.MeverSoftWhite
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.SOUNDCLOUD
import com.dapascript.mever.core.common.util.PlatformType.SPOTIFY
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC

object MeverIconAttr {
    data class MeverIconArgs(
        val icon: Int,
        val iconBackgroundColor: Color,
        val iconSize: Dp,
        val iconPadding: Dp,
    )

    fun getPlatformIcon(platform: String) = when {
        platform.contains(FACEBOOK.platformName) -> R.drawable.ic_facebook
        platform.contains(INSTAGRAM.platformName) -> R.drawable.ic_instagram
        platform.contains(PINTEREST.platformName) -> R.drawable.ic_pinterest
        platform.contains(SOUNDCLOUD.platformName) -> R.drawable.ic_soundcloud
        platform.contains(SPOTIFY.platformName) -> R.drawable.ic_spotify
        platform.contains(TERABOX.platformName) -> R.drawable.ic_terabox
        platform.contains(THREADS.platformName) -> R.drawable.ic_threads
        platform.contains(TIKTOK.platformName) -> R.drawable.ic_tiktok
        platform.contains(TWITTER.platformName) -> R.drawable.ic_twitter
        platform.contains(VIDEY.platformName) -> R.drawable.ic_videy
        platform.contains(YOUTUBE.platformName) -> R.drawable.ic_youtube
        platform.contains(YOUTUBE_MUSIC.platformName) -> R.drawable.ic_yt_music
        else -> R.drawable.ic_broken_image
    }

    fun getPlatformIconBackgroundColor(platform: String) = when {
        platform.contains(FACEBOOK.platformName) -> MeverLightBlue
        platform.contains(INSTAGRAM.platformName) -> MeverCreamPink
        platform.contains(PINTEREST.platformName) -> MeverLightPink
        platform.contains(SOUNDCLOUD.platformName) -> MeverCreamPink
        platform.contains(SPOTIFY.platformName) -> MeverGreen
        platform.contains(TERABOX.platformName) -> MeverSoftWhite
        platform.contains(THREADS.platformName) -> MeverGreen
        platform.contains(TIKTOK.platformName) -> MeverLightGreen
        platform.contains(TWITTER.platformName) -> MeverLightPurple
        platform.contains(VIDEY.platformName) -> MeverSoftGray
        platform.contains(YOUTUBE.platformName) -> MeverPink
        platform.contains(YOUTUBE_MUSIC.platformName) -> MeverPink
        else -> MeverLightGray
    }
}