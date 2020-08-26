package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call

class MovieDetailsRepository(private val movieId: (Int) -> Call<MovieDetails>) {
    private lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchMovieDetails(movieId: Int): LiveData<MovieDetails> {
        if (!this::movieDetailsDataSource.isInitialized)
            movieDetailsDataSource = MovieDetailsDataSource(this.movieId)

        movieDetailsDataSource.fetchMovieDetails(movieId)
        return movieDetailsDataSource.movieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        if (!this::movieDetailsDataSource.isInitialized)
            movieDetailsDataSource = MovieDetailsDataSource(this.movieId)

        return movieDetailsDataSource.networkState
    }
}