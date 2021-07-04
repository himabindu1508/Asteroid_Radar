package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidsDao {

    @Query("select * from asteroids_table")
    fun getAsteroids() : LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids : DatabaseAsteroid)

    @Query("delete from asteroids_table")
    fun deleteAllAsteroids()

    @Query("select * from image_of_the_day_table")
    fun getImageOfTheDay() : LiveData<DatabaseImageOfTheDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setImageOfTheDay(imageOfTheDay : DatabaseImageOfTheDay)

}

@Database(entities = [DatabaseAsteroid::class, DatabaseImageOfTheDay::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val astroidsDao : AsteroidsDao
}

private lateinit var DATABASE_INSTANCE : AsteroidsDatabase

fun getAsteroidsDatabase(context : Context) : AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if(!::DATABASE_INSTANCE.isInitialized) {
            DATABASE_INSTANCE = Room.databaseBuilder(context.applicationContext,
                                    AsteroidsDatabase::class.java,
                                        "asteroids").build()
        }
    }
    return DATABASE_INSTANCE
}



