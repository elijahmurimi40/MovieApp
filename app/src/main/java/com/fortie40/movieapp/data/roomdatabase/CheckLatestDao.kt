package com.fortie40.movieapp.data.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fortie40.movieapp.data.models.Response

@Dao
interface CheckLatestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestVersion(response: Response)

    @Query("SELECT * FROM app_version")
    suspend fun getLatestVersion(): Response
}