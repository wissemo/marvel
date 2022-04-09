package com.miled.marvel.features.favoriteComics.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miled.marvel.R
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.features.comics.repository.ComicsRepositoryImpl
import com.miled.marvel.features.comics.viewModel.ComicsViewModelState
import com.miled.marvel.utils.TestUtil
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
class FavoriteComicsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var comicsRepositoryImpl: ComicsRepositoryImpl

    private lateinit var favoriteComicsViewModel: FavoriteComicsViewModel

    private lateinit var favoriteComics: List<Comic>

    private lateinit var comicsResult: ComicsResult


    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
        comicsResult = parseJson(TestUtil.comicResultFileName)
        favoriteComics = comicsResult.comics?.results!!
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `load favorite comics success`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            coEvery {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicsLoaded(favoriteComics)
            }
            //act
            favoriteComicsViewModel = FavoriteComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            delay(10)
            //assert
            MatcherAssert.assertThat(
                favoriteComicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            MatcherAssert.assertThat(
                favoriteComicsViewModel.favoriteComicsList.value?.size,
                Matchers.equalTo(favoriteComics.size)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }

    @Test
    fun `load favorite comics failed`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            coEvery {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicsFailed(R.string.errors_cash_failed)
            }
            //act
            favoriteComicsViewModel = FavoriteComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            delay(10)
            //assert
            MatcherAssert.assertThat(
                favoriteComicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.OnError(R.string.errors_cash_failed))
            )
            MatcherAssert.assertThat(
                favoriteComicsViewModel.favoriteComicsList.value?.size,
                Matchers.equalTo(0)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }

    @Test
    fun `add favorite comics success`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            val comicToAdd = favoriteComics.first()
            val cachedComics = favoriteComics.drop(1)
            coEvery {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicsLoaded(cachedComics)
            }
            coEvery {
                comicsRepositoryImpl.cacheComic(
                    comic = comicToAdd,
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicAdded(favoriteComics)
            }
            //act
            favoriteComicsViewModel = FavoriteComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            favoriteComicsViewModel.addFavoriteComics(comic = comicToAdd)
            delay(10)
            //assert
            MatcherAssert.assertThat(
                favoriteComicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            MatcherAssert.assertThat(
                favoriteComicsViewModel.favoriteComicsList.value?.size,
                Matchers.equalTo(favoriteComics.size)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.cacheComic(
                    comic = comicToAdd,
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }

    @Test
    fun `delete favorite comics success`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            val comicToDelete = favoriteComics.first()
            val cachedComicsAfterDelete = favoriteComics.drop(1)
            coEvery {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicsLoaded(favoriteComics)
            }
            coEvery {
                comicsRepositoryImpl.deleteCachedComic(
                    comicId = comicToDelete.id!!,
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicDelete(cachedComicsAfterDelete)
            }
            //act
            favoriteComicsViewModel = FavoriteComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            favoriteComicsViewModel.deleteFavoriteComic(comicId = comicToDelete.id)
            delay(10)
            //assert
            MatcherAssert.assertThat(
                favoriteComicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            MatcherAssert.assertThat(
                favoriteComicsViewModel.favoriteComicsList.value?.size,
                Matchers.equalTo(cachedComicsAfterDelete.size)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.deleteCachedComic(
                    comicId = comicToDelete.id!!,
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }

    @Test
    fun `delete all favorite comics success`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            //arrange
            coEvery {
                comicsRepositoryImpl.getCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicsLoaded(favoriteComics)
            }
            coEvery {
                comicsRepositoryImpl.deleteCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }.coAnswers {
                favoriteComicsViewModel.onFavoriteComicDelete(emptyList())
            }
            //act
            favoriteComicsViewModel = FavoriteComicsViewModel(
                comicsRepository = comicsRepositoryImpl
            )
            //await for the function call coAnsewers
            favoriteComicsViewModel.deleteAllFavoriteComics()
            delay(10)
            //assert
            MatcherAssert.assertThat(
                favoriteComicsViewModel.comicsViewModelState.value,
                Matchers.`is`(ComicsViewModelState.IsLoaded)
            )
            MatcherAssert.assertThat(
                favoriteComicsViewModel.favoriteComicsList.value?.size,
                Matchers.equalTo(0)
            )
            coVerify(exactly = 1) {
                comicsRepositoryImpl.deleteCachedComics(
                    onSuccess = any(),
                    onError = any()
                )
            }
        }
    }
}