package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback.enqueueCallBack
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call
import retrofit2.Response

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class MovieDetailsDataSource(private val movieId: (Integer) -> Call<MovieDetails>) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse: LiveData<MovieDetails>
        get() = _movieDetailsResponse

    private fun success(response: Response<MovieDetails>) {
        _movieDetailsResponse.postValue(response.body())
        _networkState.postValue(NetworkState.LOADED)
    }

    fun fetchMovieDetails(movieId: Integer) {
        _networkState.postValue(NetworkState.LOADING)
        this.movieId(movieId).enqueueCallBack(_networkState, ::success)
    }
}