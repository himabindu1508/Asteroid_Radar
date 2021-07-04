package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NasaAsteroidsRequestStatus
import com.udacity.asteroidradar.NasaImageRequestStatus
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.domain.ImageOfTheDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception

class MainViewModel(val database : AsteroidsDatabase, application: Application) : AndroidViewModel(application) {

    //Navigate to asteroid details fragment var
    private  var _navigateToDetailsScreen = MutableLiveData<Boolean>()
    val navigateToDetailsScreen : LiveData<Boolean>
    get() = _navigateToDetailsScreen

    //Image of the day var
    private var _imageOfTheDayVar = MutableLiveData<ImageOfTheDay?>()
    val imageOfTheDayVar : LiveData<ImageOfTheDay?>
    get() = _imageOfTheDayVar

    //Asteroids var
    lateinit var asteroidsVar : LiveData<List<Asteroid>>

    //status vars
    private var _imageStatus = MutableLiveData<NasaImageRequestStatus>()
    val imageStatus : LiveData<NasaImageRequestStatus>
    get() = _imageStatus

    private var _asteroidsStatus = MutableLiveData<NasaAsteroidsRequestStatus>()
    val asteroidsStatus : LiveData<NasaAsteroidsRequestStatus>
    get() = _asteroidsStatus

    val asteroidsRepository = AsteroidsRepository(database)

    init {
        Timber.i("ViewModelInit")
        _navigateToDetailsScreen.value = false
        viewModelScope.launch {
            Timber.i("ViewModelInit launch")
            refreshPictureOfDayFromNetwork()
            refreshAsteroidsFromNetwork()
        }
    }

    private fun refreshPictureOfDayFromNetwork() {
        Timber.i("refreshPictureOfDayFromNetwork")
        viewModelScope.launch {
            try {
                Timber.i("refreshPictureOfDayFromNetwork : LOADING")
                _imageStatus.value = NasaImageRequestStatus.LOADING
                asteroidsRepository.refreshImageOfTheDay()
                _imageOfTheDayVar.value = asteroidsRepository.getImageOfTheDay()
                _imageStatus.value = NasaImageRequestStatus.DONE
                Timber.i("refreshPictureOfDayFromNetwork : DONE")
            }
            catch (e : Exception) {
                _imageStatus.value = NasaImageRequestStatus.ERROR
                Timber.i("Error in downloading image : ${e.message}")
            }
        }
    }

    private fun refreshAsteroidsFromNetwork() {
        Timber.i("refreshAsteroidsFromNetwork")
        viewModelScope.launch {
            try {
                Timber.i("refreshAsteroidsFromNetwork : DOWNLOADING")
                _asteroidsStatus.value = NasaAsteroidsRequestStatus.DOWNLOADING
                asteroidsRepository.refreshAsteroidsData()
                asteroidsVar = asteroidsRepository.asteroids
                _asteroidsStatus.value = NasaAsteroidsRequestStatus.SUCCESS
                Timber.i("refreshAsteroidsFromNetwork : SUCCESS")
            }
            catch (e : Exception) {
                _asteroidsStatus.value = NasaAsteroidsRequestStatus.FAILURE
                Timber.i("Error in downloading asteroids data : ${e.message}")
            }
        }
    }

    private fun clear() {
        viewModelScope.launch {
            asteroidsRepository.clearAsteroidsData()
        }
    }

    fun navigateToDetailsScreen(selectedAsteroid : Asteroid) {
        //Send selected args
        _navigateToDetailsScreen.value = true
    }
    fun navigateComplete() {
        _navigateToDetailsScreen.value = false
    }
}