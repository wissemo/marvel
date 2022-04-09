package com.miled.marvel.features.comics.repository

import com.miled.marvel.R
import com.miled.marvel.core.exceptions.CashException
import com.miled.marvel.core.exceptions.NoConnectivityException
import com.miled.marvel.core.exceptions.ServerException
import com.miled.marvel.features.comics.data.dataSources.localDataSource.ComicsLocalDataSourceImpl
import com.miled.marvel.features.comics.data.dataSources.localDataSource.ComicsLocalDataSourceResult
import com.miled.marvel.features.comics.data.dataSources.remoteDataSource.ComicsRemoteDataSourceImpl
import com.miled.marvel.features.comics.data.dataSources.remoteDataSource.ComicsRemoteDataSourceResult
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.utils.TestUtil
import com.miled.marvel.utils.TestUtil.comicTitleTest
import com.miled.marvel.utils.parseJson
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class ComicsRepositoryImplTest {

    @MockK
    private lateinit var comicsRemoteDataSource: ComicsRemoteDataSourceImpl

    @MockK
    private lateinit var comicsLocalDataSource: ComicsLocalDataSourceImpl

    private lateinit var comicsRepositoryImpl: ComicsRepositoryImpl
    private lateinit var comicsResult: ComicsResult

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        comicsResult = parseJson(TestUtil.comicResultFileName)
        comicsRepositoryImpl = ComicsRepositoryImpl(
            comicsLocalDataSource = comicsLocalDataSource,
            comicsRemoteDataSource = comicsRemoteDataSource
        )

    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //remote calls
    @Test
    fun `getRemoteComics success`() = runBlocking {
        //arrange
        val onSuccess = mockk<(ComicsResult) -> Unit>()
        every { onSuccess.invoke(comicsResult) } returns Unit
        val onError = mockk<(Int) -> Unit>()
        coEvery {
            comicsRemoteDataSource.getComics(
                offset = 0,
            )
        }.coAnswers {
            ComicsRemoteDataSourceResult.OnSuccess(comicsResult)
        }
        //act
        comicsRepositoryImpl.getRemoteComics(
            title = "",
            offset = 0,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onSuccess(comicsResult) }
    }

    @Test
    fun `getRemoteComics with title success`() = runBlocking {
        //arrange
        val onSuccess = mockk<(ComicsResult) -> Unit>()
        every { onSuccess.invoke(comicsResult) } returns Unit
        val onError = mockk<(Int) -> Unit>()
        coEvery {
            comicsRemoteDataSource.getComicsByTitle(
                title = comicTitleTest,
                offset = 0,
            )
        }.coAnswers {
            ComicsRemoteDataSourceResult.OnSuccess(comicsResult)
        }
        //act
        comicsRepositoryImpl.getRemoteComics(
            title = comicTitleTest,
            offset = 0,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onSuccess(comicsResult) }
    }

    @Test
    fun `getRemoteComics failure(errors_no_connexion)`() = runBlocking {
        //arrange
        val onSuccess = mockk<(ComicsResult) -> Unit>()
        val onError = mockk<(Int) -> Unit>()
        every { onError.invoke(R.string.errors_no_connexion) } returns Unit
        coEvery {
            comicsRemoteDataSource.getComics(
                offset = 0,
            )
        }.coAnswers {
            ComicsRemoteDataSourceResult.OnError(NoConnectivityException())
        }
        //act
        comicsRepositoryImpl.getRemoteComics(
            title = "",
            offset = 0,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onError(R.string.errors_no_connexion) }
    }

    @Test
    fun `getRemoteComics with title failure(errors_server_failed)`() = runBlocking {
        //arrange
        val onSuccess = mockk<(ComicsResult) -> Unit>()
        val onError = mockk<(Int) -> Unit>()
        every { onError.invoke(R.string.errors_server_failed) } returns Unit
        coEvery {
            comicsRemoteDataSource.getComicsByTitle(
                title = comicTitleTest,
                offset = 0,
            )
        }.coAnswers {
            ComicsRemoteDataSourceResult.OnError(ServerException())
        }
        //act
        comicsRepositoryImpl.getRemoteComics(
            title = comicTitleTest,
            offset = 0,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onError(R.string.errors_server_failed) }
    }

    //Local calls
    @Test
    fun `getLocalComics success`() = runBlocking {
        //arrange
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        every { onSuccess.invoke(comicsResult.comics?.results!!) } returns Unit
        val onError = mockk<(Int) -> Unit>()
        coEvery {
            comicsLocalDataSource.getLocalComics(
            )
        }.coAnswers {
            ComicsLocalDataSourceResult.OnSuccess(comicsResult.comics?.results!!)
        }
        //act
        comicsRepositoryImpl.getCachedComics(
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onSuccess(comicsResult.comics?.results!!) }
    }

    @Test
    fun `getLocalComics failure`() = runBlocking {
        //arrange
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        val onError = mockk<(Int) -> Unit>()
        every { onError.invoke(R.string.errors_cash_failed) } returns Unit

        coEvery {
            comicsLocalDataSource.getLocalComics(
            )
        }.coAnswers {
            ComicsLocalDataSourceResult.OnError(CashException())
        }
        //act
        comicsRepositoryImpl.getCachedComics(
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onError(R.string.errors_cash_failed) }
    }

    @Test
    fun `deleteCachedComics success`() = runBlocking {
        //arrange
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        every { onSuccess.invoke(emptyList()) } returns Unit
        val onError = mockk<(Int) -> Unit>()
        coEvery {
            comicsLocalDataSource.deleteAll(
            )
        }.coAnswers {
            ComicsLocalDataSourceResult.OnSuccess(emptyList())
        }
        //act
        comicsRepositoryImpl.deleteCachedComics(
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onSuccess(emptyList()) }
    }

    @Test
    fun `deleteCachedComics failure`() = runBlocking {
        //arrange
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        val onError = mockk<(Int) -> Unit>()
        every { onError.invoke(R.string.errors_cash_failed) } returns Unit

        coEvery {
            comicsLocalDataSource.deleteAll()
        }.coAnswers {
            ComicsLocalDataSourceResult.OnError(CashException())
        }
        //act
        comicsRepositoryImpl.deleteCachedComics(
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onError(R.string.errors_cash_failed) }
    }

    @Test
    fun `deleteCachedComic success`() = runBlocking {
        //arrange
        val comicId = comicsResult.comics?.results?.get(0)?.id ?: 0
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        every { onSuccess.invoke(comicsResult.comics?.results?.drop(1)!!) } returns Unit
        val onError = mockk<(Int) -> Unit>()
        coEvery {
            comicsLocalDataSource.deleteComic(comicId = comicId)
        }.coAnswers {
            ComicsLocalDataSourceResult.OnSuccess(comicsResult.comics?.results?.drop(1)!!)
        }
        //act
        comicsRepositoryImpl.deleteCachedComic(
            comicId = comicId,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onSuccess(comicsResult.comics?.results?.drop(1)!!) }
    }

    @Test
    fun `deleteCachedComic failure`() = runBlocking {
        //arrange
        val comicId = comicsResult.comics?.results?.get(0)?.id ?: 0
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        val onError = mockk<(Int) -> Unit>()
        every { onError.invoke(R.string.errors_cash_failed) } returns Unit

        coEvery {
            comicsLocalDataSource.deleteComic(comicId = comicId)
        }.coAnswers {
            ComicsLocalDataSourceResult.OnError(CashException())
        }
        //act
        comicsRepositoryImpl.deleteCachedComic(
            comicId = comicId,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onError(R.string.errors_cash_failed) }
    }

    @Test
    fun `cacheComic success`() = runBlocking {
        //arrange
        val comic = comicsResult.comics?.results?.get(0)!!
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        every { onSuccess.invoke(comicsResult.comics?.results!!) } returns Unit
        val onError = mockk<(Int) -> Unit>()
        coEvery {
            comicsLocalDataSource.cashComics(comic = comic)
        }.coAnswers {
            ComicsLocalDataSourceResult.OnSuccess(comicsResult.comics?.results!!)
        }
        //act
        comicsRepositoryImpl.cacheComic(
            comic = comic,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onSuccess(comicsResult.comics?.results!!) }
    }

    @Test
    fun `cacheComic failure`() = runBlocking {
        //arrange
        val comic = comicsResult.comics?.results?.get(0)!!
        val onSuccess = mockk<(List<Comic>) -> Unit>()
        val onError = mockk<(Int) -> Unit>()
        every { onError.invoke(R.string.errors_cash_failed) } returns Unit
        coEvery {
            comicsLocalDataSource.cashComics(comic = comic)
        }.coAnswers {
            ComicsLocalDataSourceResult.OnError(CashException())
        }
        //act
        comicsRepositoryImpl.cacheComic(
            comic = comic,
            onSuccess = onSuccess,
            onError = onError
        )
        //assert
        coVerify(exactly = 1) { onError(R.string.errors_cash_failed) }
    }
}