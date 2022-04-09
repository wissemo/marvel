package com.miled.marvel.features.comics.data.dataSources.localDataSource

import com.miled.marvel.core.exceptions.CashException
import com.miled.marvel.features.comics.data.models.Comic
import com.miled.marvel.utils.Constants.HAWK_COMICS_KEY
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComicsLocalDataSourceImpl :
    ComicsLocalDataSource {
    override suspend fun getLocalComics(): ComicsLocalDataSourceResult =
        withContext(Dispatchers.IO) {
            ComicsLocalDataSourceResult.OnSuccess(getAllComics())
        }

    override suspend fun cashComics(comic: Comic): ComicsLocalDataSourceResult =
        withContext(Dispatchers.IO) {
            val comics = getAllComics().apply {
                add(0, comic)
            }
            Hawk.put(HAWK_COMICS_KEY, comics).let {
                if (it)
                    ComicsLocalDataSourceResult.OnSuccess(comics)
                else
                    ComicsLocalDataSourceResult.OnError(CashException())
            }
        }

    override suspend fun deleteComic(comicId: Int): ComicsLocalDataSourceResult =
        withContext(Dispatchers.IO) {
            val removedComics = getAllComics().apply { removeIf { it.id == comicId } }
            Hawk.put(HAWK_COMICS_KEY, removedComics).let {
                if (it)
                    ComicsLocalDataSourceResult.OnSuccess(getAllComics())
                else
                    ComicsLocalDataSourceResult.OnError(CashException())
            }
        }

    override suspend fun deleteAll(): ComicsLocalDataSourceResult = withContext(Dispatchers.IO) {
        Hawk.delete(HAWK_COMICS_KEY).let {
            if (it)
                ComicsLocalDataSourceResult.OnSuccess(comicsResult = emptyList())
            else
                ComicsLocalDataSourceResult.OnError(CashException())
        }
    }

    private suspend fun getAllComics(): MutableList<Comic> = withContext(Dispatchers.IO) {
        Hawk.get(HAWK_COMICS_KEY, mutableListOf())
    }
}