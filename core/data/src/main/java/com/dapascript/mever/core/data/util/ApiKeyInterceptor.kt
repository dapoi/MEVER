package com.dapascript.mever.core.data.util

import android.os.Build
import com.dapascript.mever.core.data.BuildConfig.API_KEY
import com.dapascript.mever.core.data.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import javax.inject.Inject

internal class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Chain) = chain.request().let { request ->
        val urlHost = request.url.host
        if (urlHost.contains("catbox.moe")) return@let chain.proceed(request)

        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
        val builder = request
            .newBuilder()
            .addHeader("X-Package-Name", "com.dapascript.mever")
            .addHeader("X-Device-Model", deviceModel)
        if (DEBUG && API_KEY.isNotEmpty()) {
            val url = chain.request().url.newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build()
            builder.url(url)
        }
        chain.proceed(builder.build())
    }
}