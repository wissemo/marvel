package com.miled.marvel.features.comics.data.dataSources.remoteDataSource

import com.miled.marvel.core.exceptions.ServerException
import com.miled.marvel.core.network.AppApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComicsRemoteDataSourceImpl(private val appApiServices: AppApiServices) :
    ComicsRemoteDataSource {
    override suspend fun getComics(offset: Int): ComicsRemoteDataSourceResult =
        withContext(Dispatchers.IO) {
            try {
                appApiServices.getComics(
                    offset = offset
                ).let {
                    if (it.isSuccessful && it.body() != null)
                        ComicsRemoteDataSourceResult.OnSuccess(it.body()!!)
                    else
                        ComicsRemoteDataSourceResult.OnError(ServerException())
                }
            } catch (e: Exception) {
                ComicsRemoteDataSourceResult.OnError(e)
            }
        }

    override suspend fun getComicsByTitle(
        title: String,
        offset: Int
    ): ComicsRemoteDataSourceResult =
        withContext(Dispatchers.IO) {
            try {
                appApiServices.getComicsByTitle(
                    title = title,
                    offset = offset
                ).let {
                    if (it.isSuccessful && it.body() != null)
                        ComicsRemoteDataSourceResult.OnSuccess(it.body()!!)
                    else
                        ComicsRemoteDataSourceResult.OnError(ServerException())
                }
            } catch (e: Exception) {
                ComicsRemoteDataSourceResult.OnError(e)
            }
        }
}