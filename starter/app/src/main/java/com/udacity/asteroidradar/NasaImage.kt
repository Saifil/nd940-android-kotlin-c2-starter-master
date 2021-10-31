package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabaseNasaImage
import com.udacity.asteroidradar.util.getFormattedDateString

data class NasaImage(
    val url: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String
) {
    val isSupported by lazy {
        mediaType == "image"
    }
}

fun NasaImage.asDatabaseModel(
    // since we always fetch & display today's
    // image, we default the date to today
    date: String = getFormattedDateString()
) = DatabaseNasaImage(
    url = url,
    title = title,
    date = date
)
