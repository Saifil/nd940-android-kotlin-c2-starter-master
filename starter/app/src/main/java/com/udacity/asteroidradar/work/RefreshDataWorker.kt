package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NasaRepository
import retrofit2.HttpException

class RefreshDataWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "ASTEROID_CACHING_WORK"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(context)
        val repository = NasaRepository(database)

        return try {
            repository.refreshAsteroids(
                span = NasaRepository.FetchSpan.N_DAYS
            )
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}