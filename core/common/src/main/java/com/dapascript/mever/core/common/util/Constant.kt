package com.dapascript.mever.core.common.util

object Constant {
    object PlatformName {
        const val FACEBOOK = "Facebook"
        const val INSTAGRAM = "Instagram"
        const val TWITTER = "Twitter"
        const val TIKTOK = "TikTok"
        const val YOUTUBE = "YouTube"
        const val UNKNOWN = "All"
    }

    enum class PlatformType(val platformName: String) {
        FACEBOOK(PlatformName.FACEBOOK),
        INSTAGRAM(PlatformName.INSTAGRAM),
        TWITTER(PlatformName.TWITTER),
        TIKTOK(PlatformName.TIKTOK),
        YOUTUBE(PlatformName.YOUTUBE),
        UNKNOWN(PlatformName.UNKNOWN)
    }
}