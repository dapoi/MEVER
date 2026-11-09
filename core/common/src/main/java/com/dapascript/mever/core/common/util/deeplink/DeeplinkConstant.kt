package com.dapascript.mever.core.common.util.deeplink

object DeeplinkConstant {
    const val SCHEME = "app"
    const val HOST = "mever"

    object Path {
        const val HOME = "/home"
        const val IMAGE_GENERATOR = "/image-generator"
        const val SETTING_QUICK_DOWNLOAD = "/setting-quick-download"
    }

    object Query {
        const val URL = "url"
        const val ERROR = "error_msg"
        const val RESPONSES = "responses"
        const val PROMPT = "prompt"
        const val ART_STYLE = "art_style"
    }
}