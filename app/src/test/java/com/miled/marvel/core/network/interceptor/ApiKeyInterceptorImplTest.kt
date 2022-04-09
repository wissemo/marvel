package com.miled.marvel.core.network.interceptor

import com.miled.marvel.BuildConfig
import com.miled.marvel.utils.Constants.HASH_API_KEY_HEADER_KEY
import com.miled.marvel.utils.Constants.PUBLIC_API_KEY_HEADER_KEY
import com.miled.marvel.utils.Constants.TIME_STUMP_HEADER_KEY
import generateHash
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test


class ApiKeyInterceptorImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse())
        okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(ApiKeyInterceptorImpl()).build()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testHttpInterceptor() {
        //act
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()
        val request = mockWebServer.takeRequest()

        //assert keys
        MatcherAssert.assertThat(
            request.requestUrl?.queryParameterNames?.contains(TIME_STUMP_HEADER_KEY),
            Matchers.`is`(true)
        )
        MatcherAssert.assertThat(
            request.requestUrl?.queryParameterNames?.contains(PUBLIC_API_KEY_HEADER_KEY),
            Matchers.`is`(true)
        )
        MatcherAssert.assertThat(
            request.requestUrl?.queryParameterNames?.contains(HASH_API_KEY_HEADER_KEY),
            Matchers.`is`(true)
        )
        //assert keys content
        val timeStump = (request.requestUrl?.queryParameter(TIME_STUMP_HEADER_KEY) ?: "0").toLong()

        MatcherAssert.assertThat(
            request.requestUrl?.queryParameter(PUBLIC_API_KEY_HEADER_KEY),
            Matchers.equalTo(BuildConfig.API_PUBLIC_KEY)
        )

        MatcherAssert.assertThat(
            request.requestUrl?.queryParameter(HASH_API_KEY_HEADER_KEY),
            Matchers.equalTo(generateHash(timeStump = timeStump))
        )
        //assert header
        MatcherAssert.assertThat(
            request.headers.names().contains("Content-Type"),
            Matchers.`is`(true)
        )
        MatcherAssert.assertThat(
            request.headers.values("Content-Type")[0],
            Matchers.equalTo("application/json")
        )

    }
}