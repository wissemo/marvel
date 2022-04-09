package com.miled.marvel.features.comics.data.dataSources.localDataSource

import com.miled.marvel.features.comics.data.models.Comic

interface ComicsLocalDataSource {
    suspend fun getLocalComics(): ComicsLocalDataSourceResult

    suspend fun cashComics(comic: Comic): ComicsLocalDataSourceResult

    suspend fun deleteComic(comicId: Int): ComicsLocalDataSourceResult

    suspend fun deleteAll(): ComicsLocalDataSourceResult
}

