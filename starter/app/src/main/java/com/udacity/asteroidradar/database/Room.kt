package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.util.getFormattedDateString

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM databaseasteroid")
    fun getAsteroids() : LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: List<DatabaseAsteroid>)

    @Query("SELECT COUNT(*) FROM databaseasteroid")
    fun countAll() : Int
}

@Dao
interface NasaImageDao {
    @Query("SELECT * FROM databasenasaimage WHERE date = :date")
    fun getImageOfTheDay(date: String = getFormattedDateString()) : LiveData<DatabaseNasaImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: DatabaseNasaImage)

    @Query("SELECT COUNT(*) FROM databasenasaimage")
    fun countAll() : Int
}

@Database(entities = [
    DatabaseAsteroid::class,
    DatabaseNasaImage::class
], version = 2)
abstract class NasaDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val nasaImageDao: NasaImageDao
}

private lateinit var INSTANCE: NasaDatabase

fun getDatabase(context: Context): NasaDatabase {
    synchronized(NasaDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NasaDatabase::class.java,
                "asteroids"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
