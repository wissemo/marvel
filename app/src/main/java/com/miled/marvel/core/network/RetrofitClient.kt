package com.miled.marvel.core.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.miled.marvel.core.network.interceptor.ApiKeyInterceptor
import com.miled.marvel.utils.Constants
import com.miled.marvel.core.network.interceptor.ConnectivityInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Miled on 2/20/20.
 */

object RetrofitClient {
    fun getInstance(
        connectivityInterceptor: ConnectivityInterceptor,
        addKeyInterceptor: ApiKeyInterceptor
    ): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(addKeyInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}