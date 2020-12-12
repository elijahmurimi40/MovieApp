package com.fortie40.movieapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class CheckLatestVersion (
    val success: Response
)

@Entity(tableName = "app_version")
data class Response (
    @Ignore
    val status: Int = 0,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "version_code")
    var version_code: Int = 0,

    @ColumnInfo(name = "version_name")
    var version_name: String = ""
)