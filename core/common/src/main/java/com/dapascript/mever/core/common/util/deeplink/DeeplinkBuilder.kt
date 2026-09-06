package com.dapascript.mever.core.common.util.deeplink

import android.net.Uri
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.DEEPLINK_HOST
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.DEEPLINK_SCHEME

fun buildMeverDeeplink(
    path: String,
    params: Map<String, String?> = emptyMap()
): String {
    val builder = Uri.Builder()
        .scheme(DEEPLINK_SCHEME)
        .authority(DEEPLINK_HOST)
        .appendEncodedPath(path.trimStart('/'))

    params.forEach { (key, value) ->
        if (!value.isNullOrEmpty()) {
            builder.appendQueryParameter(key, value)
        }
    }

    return builder.build().toString()
}