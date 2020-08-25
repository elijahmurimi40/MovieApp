package com.fortie40.movieapp.data.retrofitservices

import androidx.lifecycle.MutableLiveData
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.data.models.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

object RetrofitCallback {
    fun <T> Call<T>.enqueueCallBack(
        networkState: MutableLiveData<NetworkState>,
        success: (Response<T>) -> Unit,
        endOfList: Boolean = false) {

        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                when (t) {
                    is IOException -> networkState.postValue(NetworkState.ERROR)
                    else -> networkState.postValue(NetworkState.ERROR)
                }
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                when {
                    response.isSuccessful && response.body() != null -> {
                        success.invoke(response)
                    }
                    response.body() == null -> {
                        if (endOfList)
                            networkState.postValue(NetworkState.END_OF_LIST)
                        else
                            networkState.postValue(NetworkState.ERROR)
                    }
                    response.code() == 401 -> networkState.postValue(NetworkState.ERROR)
                    else -> networkState.postValue(NetworkState.ERROR)
                }
            }
        })
    }

    fun movie(page: Int): Call<MovieResponse> {
        val itmDbMovies = RetrofitBuilder.buildService(ITMDbMovies::class.java)
        return itmDbMovies.getPopularMovies(page)
    }
}