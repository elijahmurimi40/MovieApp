package com.fortie40.movieapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MainRepository
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    var id: Int = 1
    private var movieList: LiveData<List<MovieResponse?>>? = null
    fun movies(movies: Call<MovieResponse>): LiveData<List<MovieResponse?>> {
        movieList = mainRepository.fetchMovies(movies)
        return movieList as LiveData<List<MovieResponse?>>
    }

    val networkState: LiveData<NetworkState> by lazy {
        mainRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return movieList?.value?.isEmpty() ?: true
    }
}