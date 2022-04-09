package com.miled.marvel.features.comics.data.dataSources.remoteDataSource

interface ComicsRemoteDataSource {
    suspend fun getComics(
        offset: Int
    ): ComicsRemoteDataSourceResult

    suspend fun getComicsByTitle(
        title: String,
        offset: Int
    ): ComicsRemoteDataSourceResult
}

