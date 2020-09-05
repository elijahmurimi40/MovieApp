package com.fortie40.movieapp.data.retrofitservices

import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.models.VideoList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
interface ITMDbMovies {
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Integer): Call<MovieDetails>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") id: Integer): Call<VideoList>
}