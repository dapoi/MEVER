package com.dapascript.mever.core.common.util

object Constant {
    object ScreenName {
        const val NOTIFICATION = "Notification"
        const val EXPLORE = "Explore"
        const val SETTING = "Setting"
    }

    object ConnectivityName {
        const val AVAILABLE = "You're online"
        const val UNAVAILABLE = "No internet connection"
        const val LOSING = "Internet connection lost"
        const val LOST = "No internet connection"
    }

    enum class PlatformType {
        FACEBOOK,
        INSTAGRAM,
        TWITTER,
        UNKNOWN
    }
}