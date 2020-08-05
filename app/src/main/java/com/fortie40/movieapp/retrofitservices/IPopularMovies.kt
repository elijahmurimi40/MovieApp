package com.fortie40.movieapp.retrofitservices

import com.fortie40.movieapp.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IPopularMovies {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MovieResponse>
}