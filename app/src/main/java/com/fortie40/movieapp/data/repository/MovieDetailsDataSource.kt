package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback.enqueueCallBack
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call
import retrofit2.Response

class MovieDetailsDataSource(private val movieId: (Int) -> Call<MovieDetails>) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse: LiveData<MovieDetails>
        get() = _movieDetailsResponse

    private fun success(response: Response<MovieDetails>) {
        _movieDetailsResponse.postValue(response.body())
    }

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        this.movieId(movieId).enqueueCallBack(_networkState, ::success)
    }
}