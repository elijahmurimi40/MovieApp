package com.fortie40.movieapp.data.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.MovieListTypeConverter

@Database(entities = [(MovieResponse::class)], version = 1, exportSchema = false)
@TypeConverters(MovieListTypeConverter::class)
abstract class MovieAppRoomDatabase : RoomDatabase() {
    abstract fun movieAppDao(): MovieAppDao

    companion object {
        @Volatile
        private var INSTANCE: MovieAppRoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: getDataBase(context).also {
                INSTANCE = it
            }
        }

        private fun getDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext, MovieAppRoomDatabase::class.java, "movie_app"
        ).fallbackToDestructiveMigration().build()
    }
}