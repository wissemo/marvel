package com.miled.marvel.features.comics.data.models

data class Comics(
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val results: List<Comic>?,
    val total: Int?
)