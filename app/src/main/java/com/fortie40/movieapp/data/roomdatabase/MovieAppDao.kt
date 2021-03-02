package com.fortie40.movieapp.data.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fortie40.movieapp.data.models.MovieResponse

@Dao
interface MovieAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieResponse(movieResponse: MovieResponse)

    @Query("SELECT * FROM movie_response")
    suspend fun getMovieResponse(): MovieResponse
}