package com.fortie40.movieapp.data.models

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val title: String,
    val runtime: Int,
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("backdrop_path")
    val backdropPath: String
)