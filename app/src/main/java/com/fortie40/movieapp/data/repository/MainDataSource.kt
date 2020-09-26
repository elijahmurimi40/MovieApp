package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback.enqueueCallBack
import com.fortie40.movieapp.helperclasses.NetworkState
import retrofit2.Call
import retrofit2.Response

class MainDataSource(private val movies: (Int) -> Call<MovieResponse>) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieResponse = MutableLiveData<List<MovieResponse?>>()
    val movieResponse: LiveData<List<MovieResponse?>>
        get() = _movieResponse

    private fun success(response: Response<MovieResponse>) {
        val mResponse = arrayListOf<MovieResponse?>()
        mResponse.add(response.body())
        _movieResponse.postValue(mResponse)
        _networkState.postValue(NetworkState.LOADED)
    }

    fun fetchMovies() {
        _networkState.postValue(NetworkState.LOADING)
        movies.invoke(1).enqueueCallBack(_networkState, ::success)
    }

    fun fetchMoviesNext(movies: Call<MovieResponse>) {
        _networkState.postValue(NetworkState.LOADING)
        movies.enqueueCallBack(_networkState, ::success)
    }
}