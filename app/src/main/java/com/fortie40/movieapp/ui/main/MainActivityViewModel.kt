package com.fortie40.movieapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.models.Movie

class MainActivityViewModel: ViewModel() {
    private val repository: MovieRepository = MovieRepository()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        repository.fetchLiveMoviePagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

}