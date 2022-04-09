package com.miled.marvel.features.comics.repository

import com.miled.marvel.features.comics.data.dataSources.localDataSource.ComicsLocalDataSource
import com.miled.marvel.features.comics.data.dataSources.localDataSource.ComicsLocalDataSourceResult
import com.miled.marvel.features.comics.data.dataSources.remoteDataSource.ComicsRemoteDataSource
import com.miled.marvel.features.comics.data.dataSources.remoteDataSource.ComicsRemoteDataSourceResult
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.utils.mapExceptionToStringId

class ComicsRepositoryImpl(
    private val comicsRemoteDataSource: ComicsRemoteDataSource,
    private val comicsLocalDataSource: ComicsLocalDataSource
) : ComicsRepository {
    override suspend fun getRemoteComics(
        title: String,
        offset: Int,
        onSuccess: (ComicsResult) -> Unit,
        onError: (Int) -> Unit
    ) {
        val result: ComicsRemoteDataSourceResult = if (title.isEmpty())
            comicsRemoteDataSource.getComics(offset = offset)
        else
            comicsRemoteDataSource.getComicsByTitle(title = title, offset = offset)
        when (result) {
            is ComicsRemoteDataSourceResult.OnSuccess ->
                onSuccess(result.comicsResult)
            is ComicsRemoteDataSourceResult.OnError -> {
                onError(result.exception.mapExceptionToStringId())
            }
        }
    }

    override suspend fun cacheComic(
        comic: Comic,
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    ) {
        when (val result: ComicsLocalDataSourceResult =
            comicsLocalDataSource.cashComics(comic = comic)) {
            is ComicsLocalDataSourceResult.OnSuccess -> onSuccess(result.comicsResult)
            is ComicsLocalDataSourceResult.OnError -> onError(result.exception.mapExceptionToStringId())
        }
    }

    override suspend fun deleteCachedComics(
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    ) {
        when (val result: ComicsLocalDataSourceResult = comicsLocalDataSource.deleteAll()) {
            is ComicsLocalDataSourceResult.OnSuccess -> onSuccess(result.comicsResult)
            is ComicsLocalDataSourceResult.OnError -> onError(result.exception.mapExceptionToStringId())
        }
    }

    override suspend fun deleteCachedComic(
        comicId: Int,
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    ) {
        when (val result: ComicsLocalDataSourceResult =
            comicsLocalDataSource.deleteComic(comicId = comicId)) {
            is ComicsLocalDataSourceResult.OnSuccess -> onSuccess(result.comicsResult)
            is ComicsLocalDataSourceResult.OnError -> onError(result.exception.mapExceptionToStringId())
        }
    }

    override suspend fun getCachedComics(
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    ) {
        when (val result: ComicsLocalDataSourceResult = comicsLocalDataSource.getLocalComics()) {
            is ComicsLocalDataSourceResult.OnSuccess -> onSuccess(result.comicsResult)
            is ComicsLocalDataSourceResult.OnError -> onError(result.exception.mapExceptionToStringId())
        }
    }
}