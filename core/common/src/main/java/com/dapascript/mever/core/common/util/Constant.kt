package com.dapascript.mever.core.common.util

object Constant {
    object PlatformName {
        const val AI = "AI"
        const val FACEBOOK = "Facebook"
        const val INSTAGRAM = "Instagram"
        const val TIKTOK = "TikTok"
        const val TWITTER = "Twitter"
        const val YOUTUBE = "YouTube"
        const val UNKNOWN = "All"
    }

    enum class PlatformType(val platformName: String) {
        AI(PlatformName.AI),
        FACEBOOK(PlatformName.FACEBOOK),
        INSTAGRAM(PlatformName.INSTAGRAM),
        TIKTOK(PlatformName.TIKTOK),
        TWITTER(PlatformName.TWITTER),
        YOUTUBE(PlatformName.YOUTUBE),
        UNKNOWN(PlatformName.UNKNOWN)
    }
}