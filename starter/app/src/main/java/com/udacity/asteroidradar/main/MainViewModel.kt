package com.udacity.asteroidradar.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NasaImage
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {
    private val database = getDatabase(context)
    private val repository = NasaRepository(database)

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.refreshImageOfTheDay()
        }
    }

    val asteroids: LiveData<List<Asteroid>> = repository.asteroids
    val imageOfTheDay: LiveData<NasaImage> = repository.imageOfTheDay
}
