package com.miled.marvel.features.comics.data.dataSources.remoteDataSource

import com.miled.marvel.core.exceptions.NoConnectivityException
import com.miled.marvel.core.exceptions.ServerException
import com.miled.marvel.core.network.AppApiServices
import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.utils.Constants
import com.miled.marvel.utils.TestUtil
import com.miled.marvel.utils.parseJson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.MalformedURLException

class ComicsRemoteDataSourceImplTest {
    @MockK
    private lateinit var appApiServices: AppApiServices

    private lateinit var comicsResult: ComicsResult

    private lateinit var comicsRemoteDataSourceImpl: ComicsRemoteDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        comicsResult = parseJson(TestUtil.comicResultFileName)
        comicsRemoteDataSourceImpl = ComicsRemoteDataSourceImpl(appApiServices = appApiServices)

    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetchComics success`() = runBlocking {
        //arrange
        coEvery { appApiServices.getComics(0, Constants.LOAD_SIZE) } returns Response.success(
            200,
            comicsResult
        )
        //act
        val result = comicsRemoteDataSourceImpl.getComics(0)
        //assert
        coVerify(exactly = 1) { appApiServices.getComics(0, Constants.LOAD_SIZE) }
        MatcherAssert.assertThat(
            result,
            Matchers.`is`(ComicsRemoteDataSourceResult.OnSuccess(comicsResult = comicsResult))
        )
    }

    @Test
    fun `fetchComics throws MalformedURLException`() = runBlocking {
        //arrange
        val exception = MalformedURLException()
        coEvery { appApiServices.getComics(0, Constants.LOAD_SIZE) } throws exception
        //act
        val result = comicsRemoteDataSourceImpl.getComics(0)
        //assert
        coVerify(exactly = 1) { appApiServices.getComics(0, Constants.LOAD_SIZE) }
        MatcherAssert.assertThat(
            result,
            Matchers.`is`(ComicsRemoteDataSourceResult.OnError(exception = exception))
        )
    }

    @Test
    fun `fetchComics throws NoConnectivityException`() = runBlocking {
        //arrange
        val exception = NoConnectivityException()
        coEvery { appApiServices.getComics(0, Constants.LOAD_SIZE) } throws exception
        //act
        val result = comicsRemoteDataSourceImpl.getComics(0)
        //assert
        coVerify(exactly = 1) { appApiServices.getComics(0, Constants.LOAD_SIZE) }
        MatcherAssert.assertThat(
            result,
            Matchers.`is`(ComicsRemoteDataSourceResult.OnError(exception = exception))
        )
    }

    @Test
    fun `fetchComics failure`() = runBlocking {
        //arrange
        coEvery { appApiServices.getComics(0, Constants.LOAD_SIZE) } returns Response.error(
            501,
            "Not Implemented".toResponseBody()
        )
        //act
        val result = comicsRemoteDataSourceImpl.getComics(0)
        //assert
        coVerify(exactly = 1) { appApiServices.getComics(0, Constants.LOAD_SIZE) }
        MatcherAssert.assertThat(
            result,
            Matchers.instanceOf(ComicsRemoteDataSourceResult.OnError::class.java)
        )
        MatcherAssert.assertThat(
            (result as ComicsRemoteDataSourceResult.OnError).exception,
            Matchers.instanceOf(ServerException::class.java)
        )
    }
}