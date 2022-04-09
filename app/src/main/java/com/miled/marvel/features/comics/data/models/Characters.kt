package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Characters(
    val available: Int?,
    val collectionURI: String?,
    val returned: Int?,
    @SerializedName("items")
    val characterList:List<Item>
):Parcelable