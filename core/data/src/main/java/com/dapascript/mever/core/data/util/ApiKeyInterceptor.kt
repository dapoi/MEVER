package com.dapascript.mever.core.data.util

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(
    @ApiKey private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Chain) = chain.request().let { request ->
        val url = request.url.newBuilder()
            .addQueryParameter("apikey", apiKey)
            .build()
        val newRequest = request.newBuilder()
            .url(url)
            .build()
        chain.proceed(newRequest)
    }
}