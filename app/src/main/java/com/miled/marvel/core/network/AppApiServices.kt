package com.miled.marvel.core.network

import com.miled.marvel.features.comics.data.models.ComicsResult
import com.miled.marvel.utils.Constants.LOAD_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Miled on 2/18/20.
 */

interface AppApiServices {

    /**
     * get comics by title
     * @author Miled
     * @param title String
     * @param offset Int
     * @return Response<String>
     */
    @GET("/v1/public/comics")
    suspend fun getComicsByTitle(
        @Query("title") title: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LOAD_SIZE
    ): Response<ComicsResult>

    /**
     * get comics
     * @author Miled
     * @param offset Int
     * @return Response<String>
     */
    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LOAD_SIZE
    ): Response<ComicsResult>

}