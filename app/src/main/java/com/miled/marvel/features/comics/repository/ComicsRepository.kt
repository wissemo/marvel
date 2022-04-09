package com.miled.marvel.features.comics.repository

import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.features.comics.data.models.ComicsResult


interface ComicsRepository {
    suspend fun getRemoteComics(
        title: String,
        offset: Int,
        onSuccess: (ComicsResult) -> Unit,
        onError: (Int) -> Unit
    )

    suspend fun cacheComic(
        comic: Comic,
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    )

    suspend fun deleteCachedComics(
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    )

    suspend fun deleteCachedComic(
        comicId: Int,
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    )

    suspend fun getCachedComics(
        onSuccess: (List<Comic>) -> Unit,
        onError: (Int) -> Unit
    )
}