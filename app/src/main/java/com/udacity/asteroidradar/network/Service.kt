package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

/* HTTP links
https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=YOUR_API_KEY
[[yyyy-mm-dd]]
https://api.nasa.gov/planetary/apod?api_key=YOUR_API_KEY
*/
private const val BASE_URL = "https://api.nasa.gov/"
private const val API_KEY = "hgRit01TlbVtYfGChR6pjoogLlAd8T4yHRWm0rQN"

const val START_DATE = "2015-09-07"
const val END_DATE = "2015-09-08"

interface NasaApiService {
    @GET("neo/rest/v1/feed?start_date=${START_DATE}&end_date=${END_DATE}&api_key=${API_KEY}")
    fun getAsteroids() : Deferred<String> //Add deferred

    @GET("planetary/apod?api_key=${API_KEY}")
    fun getImageOfTheDay() : Deferred<NetworkImageOfTheDay>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object NasaApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val nasaAPIservice : NasaApiService = retrofit.create(NasaApiService::class.java)
}