package com.fortie40.movieapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.fortie40.movieapp.data.repository.MovieListRepository
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.data.models.Movie

class ListActivityViewModel(private val movieListRepository: MovieListRepository): ViewModel() {
    var title = ""

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieListRepository.fetchLiveMoviePagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    fun invalidateList() {
        moviePagedList.value?.dataSource?.invalidate()
    }

    fun retry() {
        movieListRepository.retry()
    }
}