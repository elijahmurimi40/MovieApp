package com.fortie40.movieapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetails(
    val title: String = "",
    val runtime: Int = 0,
    val overview: String = "",

    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("backdrop_path")
    val backdropPath: String = ""
): Parcelable