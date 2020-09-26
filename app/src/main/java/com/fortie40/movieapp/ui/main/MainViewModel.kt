package com.fortie40.movieapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MainRepository
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    val movies: LiveData<List<MovieResponse?>> by lazy {
        mainRepository.fetchMovies()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mainRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return movies.value?.isEmpty() ?: true
    }

    fun getMoviesNext(movies: Call<MovieResponse>) {
        mainRepository.fetchMoviesNext(movies)
    }
}