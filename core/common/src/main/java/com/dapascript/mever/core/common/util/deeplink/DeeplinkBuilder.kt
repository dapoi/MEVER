package com.dapascript.mever.core.common.util.deeplink

import android.net.Uri
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.HOST
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.SCHEME

fun buildMeverDeeplink(
    path: String,
    params: Map<String, String?> = emptyMap()
): String {
    val builder = Uri.Builder()
        .scheme(SCHEME)
        .authority(HOST)
        .appendEncodedPath(path.trimStart('/'))

    params.forEach { (key, value) ->
        if (!value.isNullOrEmpty()) {
            builder.appendQueryParameter(key, value)
        }
    }

    return builder.build().toString()
}