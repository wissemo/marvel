package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    val extension: String?,
    val path: String?
):Parcelable {
    val coverImagePath get() = "$path.$extension"
}