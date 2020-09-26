package com.fortie40.movieapp.data.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    var title: String,
    var id: Int,

    val page: Int,

    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val movieList: List<Movie>
)