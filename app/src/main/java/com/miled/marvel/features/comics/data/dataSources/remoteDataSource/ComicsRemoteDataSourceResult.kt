package com.miled.marvel.features.comics.data.dataSources.remoteDataSource

import com.miled.marvel.features.comics.data.models.ComicsResult

sealed class ComicsRemoteDataSourceResult {
    data class OnSuccess(val comicsResult: ComicsResult) : ComicsRemoteDataSourceResult()
    data class OnError(val exception: Exception) : ComicsRemoteDataSourceResult()
}