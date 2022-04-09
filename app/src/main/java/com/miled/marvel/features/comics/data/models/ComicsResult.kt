package com.miled.marvel.features.comics.data.models

import com.google.gson.annotations.SerializedName

data class ComicsResult(
    val code: Int?,
    @SerializedName("data")
    val comics: Comics?,
    val etag: String?,
    val status: String?
)