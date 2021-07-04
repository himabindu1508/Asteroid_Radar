package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.getAsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import java.lang.Exception

class RefreshDataWorker(appCtx : Context, params : WorkerParameters) : CoroutineWorker(appCtx, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = getAsteroidsDatabase(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {
            repository.refreshAsteroidsData()
            repository.refreshImageOfTheDay()
            Result.success()
        }
        catch (e : Exception) {
            Result.retry()
        }
    }
}