package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Image(
    val extension: String?,
    val path: String?
):Parcelable