package com.fortie40.movieapp.data.retrofitservices

import com.fortie40.movieapp.data.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITMDbMovies {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MovieResponse>
}