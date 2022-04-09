package com.miled.marvel.features.comics.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miled.marvel.R
import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.features.comics.repository.ComicsRepositoryImpl
import com.miled.marvel.utils.Constants.LOAD_SIZE
import com.miled.marvel.utils.TestUtil.comicLoadMoreResultFileName
import com.miled.marvel.utils.TestUtil.comicResultFileName
import com.miled.marvel.utils.parseJson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class ComicsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var comicsRepositoryImpl: ComicsRepositoryImpl

    private lateinit var comicsViewModel: ComicsViewModel

    private lateinit var comicsResult: ComicsResult
    private lateinit var comicsResultLoadMore: ComicsResult


    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
        comicsResult = parseJson(comicResultFileName)
        comicsResultLoadMore = parseJson(comicLoadMoreResultFileName)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `first load comics success`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            coEvery {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = 0,
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                comicsViewModel.onComicsLoaded(comicsResult)
            }
            //act
            comicsViewModel = ComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            delay(10)
            //assert
            MatcherAssert.assertThat(
                comicsViewModel.comicsList.value?.size,
                Matchers.equalTo(comicsResult.comics?.results?.size)
            )
            MatcherAssert.assertThat(
                comicsViewModel.totalCount,
                Matchers.equalTo(comicsResult.comics?.total)
            )
            MatcherAssert.assertThat(
                comicsViewModel.offset,
                Matchers.equalTo(comicsResult.comics?.offset)
            )
            MatcherAssert.assertThat(
                comicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            MatcherAssert.assertThat(
                comicsViewModel.totalCount,
                Matchers.equalTo(comicsResult.comics?.total)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = 0,
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }

    @Test
    fun `first load comics failed`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            coEvery {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = 0,
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                comicsViewModel.onComicsFailed(R.string.errors_no_connexion)
            }
            //act
            comicsViewModel = ComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            delay(10)
            //assert
            MatcherAssert.assertThat(
                comicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.OnError(R.string.errors_no_connexion))
            )
            MatcherAssert.assertThat(
                comicsViewModel.comicsList.value?.size,
                Matchers.equalTo(0)
            )
            MatcherAssert.assertThat(
                comicsViewModel.totalCount,
                Matchers.equalTo(0)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = 0,
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }

    @Test
    fun `first load more`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            coEvery {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = 0,
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                comicsViewModel.onComicsLoaded(comicsResult)
            }
            coEvery {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = LOAD_SIZE,
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                comicsViewModel.onMoreComicsLoaded(comicsResultLoadMore)
            }
            //act
            comicsViewModel = ComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            delay(10)
            //assert
            MatcherAssert.assertThat(
                comicsViewModel.comicsList.value?.size,
                Matchers.equalTo(comicsResult.comics?.results?.size)
            )
            MatcherAssert.assertThat(
                comicsViewModel.totalCount,
                Matchers.equalTo(comicsResult.comics?.total)
            )
            MatcherAssert.assertThat(
                comicsViewModel.offset,
                Matchers.equalTo(comicsResult.comics?.offset)
            )
            MatcherAssert.assertThat(
                comicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            MatcherAssert.assertThat(
                comicsViewModel.totalCount,
                Matchers.equalTo(comicsResult.comics?.total)
            )
            //load more
            comicsViewModel.loadMoreComics()
            delay(10)
            MatcherAssert.assertThat(
                comicsViewModel.comicsList.value?.size,
                Matchers.equalTo(comicsResult.comics?.results?.size!! + comicsResultLoadMore.comics?.results?.size!!)
            )
            MatcherAssert.assertThat(
                comicsViewModel.offset,
                Matchers.equalTo(comicsResultLoadMore.comics?.offset)
            )
            MatcherAssert.assertThat(
                comicsViewModel.totalCount,
                Matchers.equalTo(comicsResultLoadMore.comics?.total)
            )
            MatcherAssert.assertThat(
                comicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            //load more All Loaded
            comicsViewModel.loadMoreComics()
            MatcherAssert.assertThat(
                comicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.AllLoaded)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = 0,
                    onSuccess = any(),
                    onError = any()
                )
            }
            coVerify(exactly = 1) {
                comicsRepositoryImpl.getRemoteComics(
                    title = "",
                    offset = LOAD_SIZE,
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }
}