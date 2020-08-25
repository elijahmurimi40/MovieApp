package com.fortie40.movieapp.helperclasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fortie40.movieapp.data.repository.MovieListRepository

class ViewModelFactory(private val movieListRepository: MovieListRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(movieListRepository::class.java).newInstance(movieListRepository)
    }
}