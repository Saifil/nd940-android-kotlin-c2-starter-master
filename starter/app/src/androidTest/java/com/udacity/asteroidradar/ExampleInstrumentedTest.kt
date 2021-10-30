package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.asteroidradar.database.*
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var nasaImageDao: NasaImageDao
    private lateinit var db: NasaDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, NasaDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        asteroidDao = db.asteroidDao
        nasaImageDao = db.nasaImageDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAsteroidCount() {
        val count = asteroidDao.countAll()
        assertEquals(count, 0)
    }

    @Test
    @Throws(Exception::class)
    fun insertAsteroid() {
        val asteroid = DatabaseAsteroid(
            1,
            "demo",
            "2022-12-12",
            12.0,
            2.0,
            1000.0,
            12345.567,
            false
        )
        val countPrev = asteroidDao.countAll()
        asteroidDao.insertAll(listOf(asteroid))
        val countAfter = asteroidDao.countAll()
        assertEquals(countPrev + 1, countAfter)
    }

    @Test
    @Throws(Exception::class)
    fun getNasaImageCount() {
        val count = nasaImageDao.countAll()
        assertEquals(count, 0)
    }

    @Test
    @Throws(Exception::class)
    fun insertNasaImage() {
        val image = DatabaseNasaImage(
            url = "thisisdemourl.png",
            title = "this is a demo test obj",
            date = "2021-10-29"
        )
        val countPrev = nasaImageDao.countAll()
        nasaImageDao.insert(image)
        val countAfter = nasaImageDao.countAll()
        assertEquals(countPrev + 1, countAfter)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.udacity.asteroidradar", appContext.packageName)
    }
}
