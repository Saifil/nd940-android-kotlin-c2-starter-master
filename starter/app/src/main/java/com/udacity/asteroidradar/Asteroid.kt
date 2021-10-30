package com.udacity.asteroidradar

import android.os.Parcelable
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable

fun List<Asteroid>.asDatabaseModel() : List<DatabaseAsteroid> {
    return map { domainAsteroid ->
        DatabaseAsteroid(
            id = domainAsteroid.id,
            codename = domainAsteroid.codename,
            closeApproachDate = domainAsteroid.closeApproachDate,
            absoluteMagnitude = domainAsteroid.absoluteMagnitude,
            estimatedDiameter = domainAsteroid.estimatedDiameter,
            relativeVelocity = domainAsteroid.relativeVelocity,
            distanceFromEarth = domainAsteroid.distanceFromEarth,
            isPotentiallyHazardous = domainAsteroid.isPotentiallyHazardous
        )
    }
}