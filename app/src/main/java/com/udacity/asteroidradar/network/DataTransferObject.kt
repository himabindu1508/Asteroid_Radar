package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabaseImageOfTheDay
import com.udacity.asteroidradar.database.imageIdConst

@JsonClass(generateAdapter = true)
data class NetworkImageOfTheDay(
        val url : String,
        val media_type : String,
        val title : String
)

fun NetworkImageOfTheDay.asDatabaseModel() : DatabaseImageOfTheDay {
        return DatabaseImageOfTheDay(
            imageIdConst,
            url,
            media_type,
            title
        )
}

data class NetworkAsteroidContainer(val asteroids : List<NetworkAsteroid>)

data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun ArrayList<NetworkAsteroid>.asDatabaseModel() : Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}