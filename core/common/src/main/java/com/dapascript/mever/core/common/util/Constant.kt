package com.dapascript.mever.core.common.util

object Constant {
    object ScreenName {
        const val NOTIFICATION = "Notification"
        const val GALLERY = "Gallery"
        const val SETTING = "Setting"
    }

    object ConnectivityName {
        const val AVAILABLE = "You're online"
        const val UNAVAILABLE = "No internet connection"
        const val LOSING = "Internet connection lost"
        const val LOST = "No internet connection"
    }

    object PlatformName {
        const val FACEBOOK = "Facebook"
        const val INSTAGRAM = "Instagram"
        const val TWITTER = "Twitter"
        const val TIKTOK = "TikTok"
        const val UNKNOWN = "Unknown"
    }

    enum class PlatformType(val platformName: String) {
        FACEBOOK(PlatformName.FACEBOOK),
        INSTAGRAM(PlatformName.INSTAGRAM),
        TWITTER(PlatformName.TWITTER),
        TIKTOK(PlatformName.TIKTOK),
        UNKNOWN(PlatformName.UNKNOWN)
    }
}