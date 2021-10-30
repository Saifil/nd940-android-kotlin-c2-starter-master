package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.api.getAsteroidsFromStringResult
import com.udacity.asteroidradar.api.getNasaImageFromStringResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.util.getNextSevenDaysFormattedDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NasaRepository(private val database: NasaDatabase) {

    /**
     * For rendering purpose, we use DB as the single source of truth
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) { it?.asDomainModel() }
    val imageOfTheDay: LiveData<NasaImage> =
        Transformations.map(database.nasaImageDao.getImageOfTheDay()) { it?.asDomainModel() }

    suspend fun refreshAsteroids() = withContext(Dispatchers.IO) {
        try {
            val thisWeekDates = getNextSevenDaysFormattedDates()
            // make network call to fetch the results
            val response = AsteroidApi.retrofitService.getAsteroids(
                startDate = thisWeekDates[0],
                endDate = thisWeekDates[Constants.DEFAULT_END_DATE_DAYS - 1]
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
            val response = AsteroidApi.retrofitService.getImageOfTheDay()
            val nasaImage = getNasaImageFromStringResult(response)

            if (nasaImage.isSupported) {
                database.nasaImageDao.insert(nasaImage.asDatabaseModel())
            }
        } catch (e: Exception) {
            // do nothing
        }
    }
}
