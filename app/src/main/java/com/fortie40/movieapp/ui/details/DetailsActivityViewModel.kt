package com.fortie40.movieapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.helperclasses.NetworkState

class DetailsActivityViewModel(
    private val movieDetailsRepository: MovieDetailsRepository,
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN") movieId: Integer) : ViewModel() {

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchMovieDetails(movieId)
    }

    val movieVideos: LiveData<List<Video>> by lazy {
        movieDetailsRepository.fetchMovieVideos(movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.getNetworkState()
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun refresh(movieId: Integer) {
        movieDetailsRepository.fetchMovieDetails(movieId)
    }
}