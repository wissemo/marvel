package com.miled.marvel.utils

import com.google.gson.Gson
import java.io.FileReader
import java.io.Reader


inline fun <reified T : Any> parseJson(fileName: String): T {
    val gson = Gson()
    return gson.fromJson(
        FileReader(fileName),
        T::class.java
    )
}
