package com.miled.marvel.core.network.interceptor

import com.miled.marvel.BuildConfig
import com.miled.marvel.utils.Constants.PUBLIC_API_KEY_HEADER_KEY
import com.miled.marvel.utils.Constants.HASH_API_KEY_HEADER_KEY
import com.miled.marvel.utils.Constants.TIME_STUMP_HEADER_KEY
import generateHash
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptorImpl : ApiKeyInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        System.currentTimeMillis().let {
            val url =
                original.url.newBuilder().addQueryParameter(PUBLIC_API_KEY_HEADER_KEY, BuildConfig.API_PUBLIC_KEY)
                    .addEncodedQueryParameter(TIME_STUMP_HEADER_KEY, it.toString())
                    .addEncodedQueryParameter(HASH_API_KEY_HEADER_KEY, generateHash(timeStump = it))
                    .build()
            original =
                original.newBuilder().addHeader("Content-Type", "application/json").url(url).build()
            return chain.proceed(original)
        }

    }
}