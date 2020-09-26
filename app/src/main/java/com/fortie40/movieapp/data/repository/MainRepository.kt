package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call

class MainRepository(private val movies: (Int) -> Call<MovieResponse>) {
    private lateinit var mainDataSource: MainDataSource

    private fun init() {
        if (!this::mainDataSource.isInitialized)
            mainDataSource = MainDataSource(movies)
    }

    fun fetchMovies(): LiveData<List<MovieResponse?>> {
        init()
        mainDataSource.fetchMovies()
        return mainDataSource.movieResponse
    }

    fun getNetworkState(): LiveData<NetworkState> {
        init()
        return mainDataSource.networkState
    }

    fun fetchMoviesNext(movies: Call<MovieResponse>) {
        init()
        mainDataSource.fetchMoviesNext(movies)
    }
}