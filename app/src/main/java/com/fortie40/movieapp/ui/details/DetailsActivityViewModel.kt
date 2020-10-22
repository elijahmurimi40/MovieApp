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
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN") private val movieId: Integer) : ViewModel() {

    private var load = true
    var movieDetails: MutableLiveData<MovieDetails>
    var movieVideos: MutableLiveData<List<Video>>
    init {
        movieDetails = fetchMovieDetails()
        movieVideos = fetchMovieVideos()
    }

    private fun fetchMovieDetails(): MutableLiveData<MovieDetails> {
        var movieDetails = MutableLiveData<MovieDetails>()
        if (load)
            movieDetails = movieDetailsRepository.fetchMovieDetails(movieId) as MutableLiveData
        return movieDetails
    }

    private fun fetchMovieVideos(): MutableLiveData<List<Video>> {
        var movieVideos = MutableLiveData<List<Video>>()
        if (load)
            movieVideos = movieDetailsRepository.fetchMovieVideos(movieId) as MutableLiveData<List<Video>>
        return movieVideos
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.getNetworkState()
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun refresh(movieId: Integer) {
        movieDetails.value = null
        movieVideos.value = null
        movieDetails = movieDetailsRepository.fetchMovieDetails(movieId) as MutableLiveData<MovieDetails>
        movieVideos = movieDetailsRepository.fetchMovieVideos(movieId) as MutableLiveData<List<Video>>
        movieDetailsRepository.fetchMovieVideos(movieId)
    }
}