package com.fortie40.movieapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.helperclasses.NetworkState

class DetailsActivityViewModel(private val movieDetailsRepository: MovieDetailsRepository, movieId: Int)
    : ViewModel() {
    val movieDetails: LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchMovieDetails(movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.getMovieDetailsNetworkState()
    }
}