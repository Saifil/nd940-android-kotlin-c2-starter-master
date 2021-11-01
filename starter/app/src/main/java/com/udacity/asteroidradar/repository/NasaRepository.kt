package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.api.getAsteroidsFromStringResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.util.getNDaysFormattedDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NasaRepository(private val database: NasaDatabase) {

    /** we don't always want to fetch 7 days of data at once
     *
     * for devices w/ slower connections, can face consistent http failure. Thus, we
     * want to only fetch first 1-2 days of asteroid data as a fallback mechanism
     */
    enum class FetchSpan {
        TODAY,
        N_DAYS
    }

    /**
     * For rendering purpose, we use DB as the single source of truth
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) { it?.asDomainModel() }
    val imageOfTheDay: LiveData<NasaImage> =
        Transformations.map(database.nasaImageDao.getImageOfTheDay()) { it?.asDomainModel() }

    suspend fun refreshAsteroids(
        span: FetchSpan = FetchSpan.TODAY
    ) = withContext(Dispatchers.IO) {
        try {
            val thisWeekDates = getNDaysFormattedDates()
            // make network call to fetch the results

            // decide the end date based on the span passed
            // just fetch 1 day(s) of data when TODAY is passed
            val endIdx = if (span == FetchSpan.TODAY) 0 else Constants.DEFAULT_END_DATE_DAYS - 1
            val response = AsteroidApi.retrofitService.getAsteroids(
                startDate = thisWeekDates[0],
                endDate = thisWeekDates[endIdx]
            )
            val asteroids = getAsteroidsFromStringResult(response)

            // insert the Asteroid into database
            database.asteroidDao.insertAll(asteroids.asDatabaseModel())
        } catch (e: Exception) {
            // do nothing
        }
    }

    suspend fun refreshImageOfTheDay() = withContext(Dispatchers.IO) {
        try {
            val nasaImage = AsteroidApi.moshiRetrofitService.getImageOfTheDay()

            // do not store the image in database if it is not of type "image"
            if (nasaImage.isSupported) {
                database.nasaImageDao.insert(nasaImage.asDatabaseModel())
            }
        } catch (e: Exception) {
            // do nothing
        }
    }
}
