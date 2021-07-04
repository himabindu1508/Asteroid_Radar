package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.domain.ImageOfTheDay

const val imageIdConst : Int = 100

@Entity(tableName = "image_of_the_day_table")
data class DatabaseImageOfTheDay(
        @PrimaryKey
        val imageId: Int = imageIdConst,
        val url: String,
        @ColumnInfo(name = "mediaType")
        val media_type: String,
        val title: String
)

fun DatabaseImageOfTheDay.asDomainModel() : ImageOfTheDay {
        return ImageOfTheDay(url, media_type, title)
}

@Entity(tableName = "asteroids_table")
data class DatabaseAsteroid constructor(
        @PrimaryKey
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel() : List<Asteroid> {
        return map {
                Asteroid (
                        id = it.id,
                        codename = it.codename,
                        closeApproachDate = it.closeApproachDate,
                        absoluteMagnitude = it.absoluteMagnitude,
                        estimatedDiameter = it.estimatedDiameter,
                        relativeVelocity = it.relativeVelocity,
                        distanceFromEarth = it.distanceFromEarth,
                        isPotentiallyHazardous = it.isPotentiallyHazardous
                )
        }
}
