package com.udacity.asteroidradar

import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun dbAsteroid_toDomain() {
        val dbAsteroid = DatabaseAsteroid(
            -1,
            "demo",
            "2022-12-12",
            12.0,
            2.0,
            1000.0,
            12345.567,
            false
        )
        val domainAsteroid = listOf(dbAsteroid).asDomainModel()[0]
        assertEquals(dbAsteroid.id, domainAsteroid.id)
    }
}
