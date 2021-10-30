package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.util.getNextSevenDaysFormattedDates
import org.json.JSONObject
import kotlin.collections.ArrayList

private fun JSONObject.parseAsteroidsJsonResult() : List<Asteroid> {
    val nearEarthObjectsJson = getJSONObject("near_earth_objects")

    val asteroidList = mutableListOf<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = Asteroid(
                id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
            )
            asteroidList.add(asteroid)
        }
    }

    return asteroidList
}

private fun JSONObject.parseNasaImageJsonResult() : NasaImage {
    val url = getString("url")
    val type = getString("media_type")
    val title = getString("title")

    return NasaImage(
        url = url,
        mediaType = type,
        title = title,
        isSupported = type == "image"
    )
}

fun getAsteroidsFromStringResult(result: String) = JSONObject(result).parseAsteroidsJsonResult()

fun getNasaImageFromStringResult(result: String) = JSONObject(result).parseNasaImageJsonResult()