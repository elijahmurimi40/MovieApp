package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.data.models.VideoList
import com.fortie40.movieapp.data.retrofitservices.ITMDbMovies
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback.enqueueCallBack
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Response

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class MovieDetailsDataSource(private val itmDbMovies: () -> ITMDbMovies) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse: LiveData<MovieDetails>
        get() = _movieDetailsResponse
    private fun successMovieDetails(response: Response<MovieDetails>) {
        _movieDetailsResponse.postValue(response.body())
        _networkState.postValue(NetworkState.LOADED)
    }
    fun fetchMovieDetails(movieId: Integer) {
        _networkState.postValue(NetworkState.LOADING)
        this.itmDbMovies().getMovieDetails(movieId).enqueueCallBack(_networkState, ::successMovieDetails)
    }

    private val _video = MutableLiveData<List<Video>>()
    val video: LiveData<List<Video>>
        get() = _video
    private fun successMovieVideos(response: Response<VideoList>) {
        _video.postValue(response.body()?.results)
        _networkState.postValue(NetworkState.LOADED)
    }
    fun fetchMovieVideos(movieId: Integer) {
        _networkState.postValue(NetworkState.LOADING)
        this.itmDbMovies().getMovieVideos(movieId).enqueueCallBack(_networkState, ::successMovieVideos)
    }
}