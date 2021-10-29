package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.api.getAsteroidsFromStringResult
import com.udacity.asteroidradar.api.getNasaImageFromStringResult
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.util.getNextSevenDaysFormattedDates
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {
    val errorEvent: LiveData<String?>
        get() = _errorEvent
    val loadingEvent: LiveData<Boolean>
        get() = _loadingEvent
    val asteroids: LiveData<ArrayList<Asteroid>>
        get() = _asteroids
    val imageOTD: LiveData<NasaImage>
        get() = _imageOTD

    private val _errorEvent = MutableLiveData<String?>()
    private val _loadingEvent = MutableLiveData<Boolean>()
    private val _asteroids = MutableLiveData<ArrayList<Asteroid>>()
    private val _imageOTD = MutableLiveData<NasaImage>()

    init {
        fetchAsteroid()
    }

    fun fetchNasaImageOfTheDay() {
        viewModelScope.launch {
            try {
                val response = AsteroidApi.retrofitService.getImageOfTheDay()
                _imageOTD.value = getNasaImageFromStringResult(response)
            } catch (e: Exception) {
                // send a null single to hide the loading spinner
                _imageOTD.value = null
                _errorEvent.value = e.message
            }
        }
    }

    private fun fetchAsteroid() {
        _loadingEvent.value = true
        viewModelScope.launch {
            val thisWeekDates =  getNextSevenDaysFormattedDates()
            try {
                val response = AsteroidApi.retrofitService.getAsteroids(
                    // pick the first and last day of the week
                    startDate = thisWeekDates[0],
                    endDate = thisWeekDates[Constants.DEFAULT_END_DATE_DAYS - 1]
                )
                _loadingEvent.value = false
                // notify the view about the asteroids
                _asteroids.value = getAsteroidsFromStringResult(response)
            } catch (e: Exception) {
                _loadingEvent.value = false
                _errorEvent.value = e.message
            }
        }
    }
}