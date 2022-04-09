package com.miled.marvel.features.comics.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comic(
    val characters: Characters?,
    val creators: Creators?,
    val collectedIssues: List<CollectedIssue>?,
    val description: String?,
    val diamondCode: String?,
    val digitalId: Int?,
    val ean: String?,
    val format: String?,
    val id: Int?,
    val images: List<Image>?,
    val isbn: String?,
    val issn: String?,
    val issueNumber: Int?,
    val modified: String?,
    val pageCount: Int?,
    val prices: List<Price>?,
    val resourceURI: String?,
    val series: Series?,
    val title: String?,
    val upc: String?,
    val thumbnail: Thumbnail?,
    val variantDescription: String?,
):Parcelable {
    val comicPrice get() = "${prices?.get(0)?.price ?: ""} $"
    val safePageCount get() = "${pageCount ?: 0}"
}