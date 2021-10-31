package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.NasaImage
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : String

    // not using the normal standard scalar way with manual parsing
//    @GET("planetary/apod")
//    suspend fun getImageOfTheDay(
//        @Query("api_key") apiKey: String = Constants.API_KEY
//    ) : String
}

/**
 * To satisfy the project requirement, we will parse the Image of the Day separately using Moshi
 *
 * From Udacity project requirement:
 * > Download Picture of Day JSON, parse it using Moshi and
 *   display it at the top of Main screen using Picasso Library.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val moshiRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface ImageApiService {
    @GET("planetary/apod")
    suspend fun getImageOfTheDay(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : NasaImage
}

/** ------------------------------------------------------------------------------------------ */

// common API object
object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }

    val moshiRetrofitService: ImageApiService by lazy {
        moshiRetrofit.create(ImageApiService::class.java)
    }
}

