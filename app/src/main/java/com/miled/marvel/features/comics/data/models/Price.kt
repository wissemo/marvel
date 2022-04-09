package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    val price: Double?,
    val type: String?
):Parcelable