package com.fortie40.movieapp.models

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,

    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: String,
    @SerializedName("release_date")
    val releaseDate: String
)