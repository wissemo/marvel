package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Series(
    val name: String?,
    val resourceURI: String?
):Parcelable