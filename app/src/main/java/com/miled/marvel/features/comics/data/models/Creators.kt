package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creators(
    val available: Int?,
    val collectionURI: String?,
    val returned: Int?,
    @SerializedName("items")
    val creatorsList: List<Item>?
):Parcelable