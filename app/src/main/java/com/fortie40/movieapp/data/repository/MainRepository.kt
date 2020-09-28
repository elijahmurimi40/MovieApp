package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call

class MainRepository {
    private lateinit var mainDataSource: MainDataSource

    private fun init() {
        if (!this::mainDataSource.isInitialized)
            mainDataSource = MainDataSource()
    }

    fun fetchMovies(movies: Call<MovieResponse>): LiveData<List<MovieResponse?>> {
        init()
        mainDataSource.fetchMovies(movies)
        return mainDataSource.movieResponse
    }

    fun getNetworkState(): LiveData<NetworkState> {
        init()
        return mainDataSource.networkState
    }
}