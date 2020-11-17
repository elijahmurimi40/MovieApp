package com.fortie40.movieapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MovieListRepository
import com.fortie40.movieapp.helperclasses.NetworkState

class ListActivityViewModel(private val movieListRepository: MovieListRepository): ViewModel() {
    var title = ""

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieListRepository.fetchLiveMoviePagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieListRepository.getNetworkState()
    }

    val movieResponse: LiveData<MovieResponse> by lazy {
        movieListRepository.getMovieResponse()
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

    suspend fun saveMovieResponse(movieResponse: MovieResponse) {
        movieListRepository.saveMovieResponse(movieResponse)
    }

    suspend fun getMovieResponseByPage(page: Int): MovieResponse {
        return movieListRepository.getMovieResponseByPage(page)
    }
}