package com.fortie40.movieapp.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,

    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val movieList: List<Movie>
)