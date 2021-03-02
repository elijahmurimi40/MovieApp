package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fortie40.movieapp.FIRST_PAGE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback.enqueueCallBack
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.roomdatabase.MovieAppDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import kotlin.properties.Delegates

class MovieListDataSource(
    private val moviesPage: (Int) -> Call<MovieResponse>,
    private val dao: MovieAppDao
) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        var lastPage = 0
    }

    private val page = FIRST_PAGE
    private var mParams: LoadParams<Int> by Delegates.notNull()
    private var mCallback by Delegates.notNull<LoadCallback<Int, Movie>>()
    private var movieList : MutableList<Movie> = arrayListOf()

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieResponse = MutableLiveData<MovieResponse>()
    val movieResponse: LiveData<MovieResponse>
        get() = _movieResponse

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>
    ) {
        _networkState.postValue(NetworkState.LOADING)

        fun success(response: Response<MovieResponse>) {
            movieList.addAll(response.body()!!.movieList)
            val movieResponse = MovieResponse(id = 1, movieList = movieList)
            CoroutineScope(IO).launch {
                dao.saveMovieResponse(movieResponse)
                withContext(Main) {
                    callback.onResult(response.body()!!.movieList, null, page + 1)
                    lastPage = page
                    _movieResponse.postValue(response.body())
                    _networkState.postValue(NetworkState.LOADED)
                }
            }
        }

        if (lastPage == 0)
            moviesPage.invoke(page).enqueueCallBack(_networkState, ::success)
        else {
            CoroutineScope(IO).launch {
                val mr = dao.getMovieResponse()
                withContext(Main) {
                    movieList = mr.movieList as MutableList<Movie>
                    callback.onResult(movieList, null, lastPage + 1)
                    _networkState.postValue(NetworkState.LOADED)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        // _networkState.postValue(NetworkState.LOADING)
        mParams = params
        mCallback = callback

        /**
        fun success(response: Response<MovieResponse>) {
            val hasMore = response.body()!!.totalPages >= params.key
            if (hasMore) {
                callback.onResult(response.body()!!.movieList, params.key + 1)
                _networkState.postValue(NetworkState.LOADED)
            } else {
                _networkState.postValue(NetworkState.END_OF_LIST)
            }
        }

        moviesPage(params.key).enqueueCallBack(_networkState, ::success, true)
        */
        loadAfterE(params, callback)
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

        moviePages(params.key).enqueueCallBack(_networkState, ::success)
        */
    }

    private fun loadAfterE(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        _networkState.postValue(NetworkState.LOADING)

        val success : (Response<MovieResponse>) -> Unit = { response: Response<MovieResponse> ->
            val hasMore = response.body()!!.totalPages >= params.key
            if (hasMore) {
                movieList.addAll(response.body()!!.movieList)
                val movieResponse = MovieResponse(id = 1, movieList = movieList)
                CoroutineScope(IO).launch {
                    dao.saveMovieResponse(movieResponse)
                    withContext(Main) {
                        callback.onResult(response.body()!!.movieList, params.key + 1)
                        lastPage = params.key
                        _movieResponse.postValue(response.body())
                        _networkState.postValue(NetworkState.LOADED)
                    }
                }
            } else {
                _networkState.postValue(NetworkState.END_OF_LIST)
            }
        }

        moviesPage(params.key).enqueueCallBack(_networkState, success, true)
    }

    fun retry() {
        loadAfterE(mParams, mCallback)
    }
}