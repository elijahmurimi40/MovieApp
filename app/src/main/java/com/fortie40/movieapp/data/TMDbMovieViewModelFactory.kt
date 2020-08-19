package com.fortie40.movieapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TMDbMovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(repository::class.java).newInstance(repository)
    }
}