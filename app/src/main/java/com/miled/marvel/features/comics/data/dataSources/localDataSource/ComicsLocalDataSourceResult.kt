package com.miled.marvel.features.comics.data.dataSources.localDataSource

import com.miled.marvel.features.comics.data.models.Comic

sealed class ComicsLocalDataSourceResult {
    data class OnSuccess(val comicsResult: List<Comic>) : ComicsLocalDataSourceResult()
    data class OnError(val exception: Exception) : ComicsLocalDataSourceResult()
}