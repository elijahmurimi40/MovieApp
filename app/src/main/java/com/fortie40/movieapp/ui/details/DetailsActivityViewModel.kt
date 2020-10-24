package com.fortie40.movieapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.helperclasses.NetworkState

class DetailsActivityViewModel(
    private val movieDetailsRepository: MovieDetailsRepository,
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN") movieId: Integer) : ViewModel() {

    val movieDetails: MutableLiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchMovieDetails(movieId) as MutableLiveData
    }

    val movieVideos: MutableLiveData<List<Video>> by lazy {
        movieDetailsRepository.fetchMovieVideos(movieId) as MutableLiveData
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.getNetworkState()
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun refresh(movieId: Integer) {
        movieDetails.value = null
        movieVideos.value = null
        movieDetailsRepository.fetchMovieDetails(movieId)
        movieDetailsRepository.fetchMovieVideos(movieId)
    }
}