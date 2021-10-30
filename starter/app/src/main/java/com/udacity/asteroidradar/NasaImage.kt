package com.udacity.asteroidradar

import com.udacity.asteroidradar.database.DatabaseNasaImage
import com.udacity.asteroidradar.util.getFormattedDateString

data class NasaImage(
    val url: String,
    val mediaType: String,
    val title: String,
    val isSupported: Boolean = true
)

fun NasaImage.asDatabaseModel(
    // since we always fetch & display today's
    // image, we default the date to today
    date: String = getFormattedDateString()
) = DatabaseNasaImage(
    url = url,
    title = title,
    date = date
)
