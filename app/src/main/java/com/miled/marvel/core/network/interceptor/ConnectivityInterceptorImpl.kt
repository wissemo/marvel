package com.miled.marvel.core.network.interceptor

import com.miled.marvel.core.exceptions.NoConnectivityException
import isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response


class ConnectivityInterceptorImpl :
    ConnectivityInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isInternetAvailable()) {
            return chain.proceed(chain.request())
        }
        throw NoConnectivityException()
    }
}
