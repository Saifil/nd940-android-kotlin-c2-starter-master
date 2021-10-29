package com.udacity.asteroidradar

data class NasaImage(
    val url: String,
    val mediaType: String,
    val title: String,
    val isSupported: Boolean = true
)