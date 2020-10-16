package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call

class MainRepository {
    private val mainDataSource: MainDataSource = MainDataSource()

    fun fetchMovies(movies: Call<MovieResponse>): LiveData<List<MovieResponse?>> {
        mainDataSource.fetchMovies(movies)
        return mainDataSource.movieResponse
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return mainDataSource.networkState
    }
}