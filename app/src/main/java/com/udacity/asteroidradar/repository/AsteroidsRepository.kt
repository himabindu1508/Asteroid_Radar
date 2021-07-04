package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.ImageOfTheDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidsRepository(private val database : AsteroidsDatabase)
{
    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.astroidsDao.getAsteroids()) {
        it.asDomainModel()
    }

    val imageOfTheDay : LiveData<ImageOfTheDay> = Transformations.map(database.astroidsDao.getImageOfTheDay()) {
        it.asDomainModel()
    }

    suspend fun getImageOfTheDay() : ImageOfTheDay? {
        val temp = database.astroidsDao.getImageOfTheDay()
        Timber.i("Image : url = ${temp.value?.url} media_type = ${temp.value?.media_type} title = ${temp.value?.title}")
        return temp.value?.let { ImageOfTheDay(it.url, it.media_type, it.title) }
    }

    suspend fun refreshAsteroidsData() {
        withContext(Dispatchers.IO) {
            val asteroidsJSONResult = NasaApi.nasaAPIservice.getAsteroids().await()
            val asteroidsDataSet = parseAsteroidsJsonResult(JSONObject(asteroidsJSONResult))
            database.astroidsDao.insertAllAsteroids(*asteroidsDataSet.asDatabaseModel())
        }
    }

    suspend fun refreshImageOfTheDay() {
        withContext(Dispatchers.IO) {
            val imageObj = NasaApi.nasaAPIservice.getImageOfTheDay().await()
            database.astroidsDao.setImageOfTheDay(imageObj.asDatabaseModel())
        }
    }

    suspend fun clearAsteroidsData() {
        database.astroidsDao.deleteAllAsteroids()
    }




}