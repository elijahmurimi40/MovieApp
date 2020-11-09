package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fortie40.movieapp.POST_PER_PAGE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import retrofit2.Call

class MovieListRepository(moviesPage: (Int) -> Call<MovieResponse>) {
    private val movieListDataSourceFactory: MovieListDataSourceFactory = MovieListDataSourceFactory(moviesPage)
    private lateinit var moviePagedList: LiveData<PagedList<Movie>>

    fun fetchLiveMoviePagedList(): LiveData<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieListDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieListDataSource, NetworkState>(
            movieListDataSourceFactory.movieLiveListDataSource, MovieListDataSource::networkState
        )
    }

    fun retry() {
        movieListDataSourceFactory.retry()
    }
}