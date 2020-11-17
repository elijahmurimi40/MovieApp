package com.fortie40.movieapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movie_response")
@Parcelize
data class MovieResponse(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "page")
    val page: Int = 0,

    @ColumnInfo(name = "total_results")
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @ColumnInfo(name = "total_pages")
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @ColumnInfo(name = "results")
    @SerializedName("results")
    val movieList: List<Movie> = arrayListOf()
): Parcelable