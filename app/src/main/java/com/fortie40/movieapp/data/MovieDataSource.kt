package com.fortie40.movieapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fortie40.movieapp.FIRST_PAGE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.helperclasses.RetrofitCallback.enqueueCallBack
import com.fortie40.movieapp.models.Movie
import com.fortie40.movieapp.models.MovieResponse
import retrofit2.Call
import retrofit2.Response

class MovieDataSource(private val movie: (Int) -> Call<MovieResponse>)
    : PageKeyedDataSource<Int, Movie>() {

    private val page = FIRST_PAGE

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>
    ) {
        _networkState.postValue(NetworkState.LOADING)

        fun success(response: Response<MovieResponse>) {
            callback.onResult(response.body()!!.movieList, null, page + 1)
            _networkState.postValue(NetworkState.LOADED)
        }

        movie.invoke(page).enqueueCallBack(_networkState, ::success)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        _networkState.postValue(NetworkState.LOADING)

        fun success(response: Response<MovieResponse>) {
            val hasMore = response.body()!!.totalPages >= params.key
            if (hasMore) {
                callback.onResult(response.body()!!.movieList, params.key + 1)
                _networkState.postValue(NetworkState.LOADED)
            } else {
                _networkState.postValue(NetworkState.END_OF_LIST)
            }
        }

        movie(params.key).enqueueCallBack(_networkState, ::success, true)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        /**
         * called when the user is scrolling up
         * no reason to implement load before since
         * the recycler view will hold the previous data
         * but will implement it anyway
         */

        /**
        fun success(response: Response<MovieResponse>) {
            val key = if (params.key > 1) params.key - 1 else 0
            if (response.body() != null) {
                callback.onResult(response.body()!!.movieList, key)
                _networkState.postValue(NetworkState.LOADED)
            }
        }

        movie(params.key).enqueueCallBack(_networkState, ::success)
        */
    }
}