package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.data.retrofitservices.ITMDbMovies
import com.fortie40.movieapp.helperclasses.NetworkState

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class MovieDetailsRepository(private val itmDbMovies: () -> ITMDbMovies) {
    private val movieDetailsDataSource: MovieDetailsDataSource = MovieDetailsDataSource(this.itmDbMovies)

    fun fetchMovieDetails(movieId: Integer): LiveData<MovieDetails> {
        movieDetailsDataSource.fetchMovieDetails(movieId)
        return movieDetailsDataSource.movieDetailsResponse
    }

    fun fetchMovieVideos(movieId: Integer): LiveData<List<Video>> {
        movieDetailsDataSource.fetchMovieVideos(movieId)
        return movieDetailsDataSource.video
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return movieDetailsDataSource.networkState
    }
}