package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NasaImage

@Entity
data class DatabaseAsteroid(
    @PrimaryKey val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel() : List<Asteroid> {
    return map { dbAsteroid ->
        Asteroid(
            id = dbAsteroid.id,
            codename = dbAsteroid.codename,
            closeApproachDate = dbAsteroid.closeApproachDate,
            absoluteMagnitude = dbAsteroid.absoluteMagnitude,
            estimatedDiameter = dbAsteroid.estimatedDiameter,
            relativeVelocity = dbAsteroid.relativeVelocity,
            distanceFromEarth = dbAsteroid.distanceFromEarth,
            isPotentiallyHazardous = dbAsteroid.isPotentiallyHazardous
        )
    }
}

@Entity
data class DatabaseNasaImage(
    @PrimaryKey val url: String,
    val title: String,
    // here for simplification, we store the Date as a string as we want exact
    // string matching and do not need any Date comparison in the query
    val date: String // date here is the date to which this image belongs to
)

fun DatabaseNasaImage.asDomainModel() = NasaImage(
    url = url,
    title = title,
    // we only store image media type in the database
    mediaType = "image",
    // image types are supported
    isSupported = true
)
