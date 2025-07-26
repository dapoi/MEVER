package com.dapascript.mever.core.data.util

import com.dapascript.mever.core.data.BuildConfig.API_KEY
import com.dapascript.mever.core.data.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Chain) = chain.request().let { request ->
        if (DEBUG && API_KEY.isNotEmpty()) {
            val url = request.url.newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build()
            val newRequest = request.newBuilder()
                .url(url)
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}