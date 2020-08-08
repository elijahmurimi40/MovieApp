package com.fortie40.movieapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fortie40.movieapp.FIRST_PAGE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.models.Movie
import com.fortie40.movieapp.models.MovieResponse
import com.fortie40.movieapp.retrofitservices.IPopularMovies
import com.fortie40.movieapp.retrofitservices.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MovieDataSource: PageKeyedDataSource<Int, Movie>() {
    private val page = FIRST_PAGE

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>
    ) {
        _networkState.postValue(NetworkState.LOADING)

        RetrofitBuilder.buildService(IPopularMovies::class.java)
            .getPopularMovies(page)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    when (t) {
                        is IOException -> _networkState.postValue(NetworkState.ERROR)
                        else -> _networkState.postValue(NetworkState.ERROR)
                    }
                }

                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>
                ) {
                    when {
                        response.isSuccessful && response.body() != null -> {
                            callback.onResult(response.body()!!.movieList, null, page + 1)
                            _networkState.postValue(NetworkState.LOADED)
                        }
                        response.code() == 401 -> _networkState.postValue(NetworkState.ERROR)
                        else -> _networkState.postValue(NetworkState.ERROR)
                    }
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        _networkState.postValue(NetworkState.LOADING)

        RetrofitBuilder.buildService(IPopularMovies::class.java)
            .getPopularMovies(params.key)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    when (t) {
                        is IOException -> _networkState.postValue(NetworkState.ERROR)
                        else -> _networkState.postValue(NetworkState.ERROR)
                    }
                }

                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>
                ) {
                    when {
                        response.isSuccessful && response.body() != null -> {
                            val hasMore = response.body()!!.totalPages >= params.key
                            if (hasMore) {
                                callback.onResult(response.body()!!.movieList, params.key + 1)
                                _networkState.postValue(NetworkState.LOADED)
                            } else {
                                _networkState.postValue(NetworkState.END_OF_LIST)
                            }
                        }
                        response.body() == null -> _networkState.postValue(NetworkState.END_OF_LIST)
                        response.code() == 401 -> _networkState.postValue(NetworkState.ERROR)
                        else -> _networkState.postValue(NetworkState.ERROR)
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        /**
         * called when the user is scrolling up
         * no reason to implement load before since
         * the recycler view will hold the previous data
         * but will implement it anyway
         */
    }
}