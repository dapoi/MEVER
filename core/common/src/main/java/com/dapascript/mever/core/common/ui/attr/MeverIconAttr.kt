package com.dapascript.mever.core.common.ui.attr

import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.APPLE_MUSIC
import com.dapascript.mever.core.common.util.PlatformType.DOUYIN
import com.dapascript.mever.core.common.util.PlatformType.EXPLORE
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.PIXIV
import com.dapascript.mever.core.common.util.PlatformType.SOUNDCLOUD
import com.dapascript.mever.core.common.util.PlatformType.SPOTIFY
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.X
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC

object MeverIconAttr {
    fun getPlatformIcon(platform: String) = when {
        platform.contains(AI.platformName) -> R.drawable.ic_awesome
        platform.contains(APPLE_MUSIC.platformName) -> R.drawable.ic_apple
        platform.contains(DOUYIN.platformName) -> R.drawable.ic_tiktok
        platform.contains(EXPLORE.platformName) -> R.drawable.ic_language
        platform.contains(FACEBOOK.platformName) -> R.drawable.ic_facebook
        platform.contains(INSTAGRAM.platformName) -> R.drawable.ic_instagram
        platform.contains(PINTEREST.platformName) -> R.drawable.ic_pinterest
        platform.contains(PIXIV.platformName) -> R.drawable.ic_pixiv
        platform.contains(SOUNDCLOUD.platformName) -> R.drawable.ic_soundcloud
        platform.contains(SPOTIFY.platformName) -> R.drawable.ic_spotify
        platform.contains(TERABOX.platformName) -> R.drawable.ic_terabox
        platform.contains(THREADS.platformName) -> R.drawable.ic_threads
        platform.contains(TIKTOK.platformName) -> R.drawable.ic_tiktok
        platform.contains(VIDEY.platformName) -> R.drawable.ic_videy
        platform.contains(X.platformName) -> R.drawable.ic_x
        platform.contains(YOUTUBE.platformName) -> R.drawable.ic_youtube
        platform.contains(YOUTUBE_MUSIC.platformName) -> R.drawable.ic_yt_music
        else -> R.drawable.ic_broken_image
    }
}