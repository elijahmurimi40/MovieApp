package com.fortie40.movieapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fortie40.movieapp.POST_PER_PAGE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.models.Movie
import com.fortie40.movieapp.models.MovieResponse
import retrofit2.Call

class MovieRepository(private val movie: (Int) -> Call<MovieResponse>) {
    private lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(): LiveData<PagedList<Movie>> {
        if (!this::moviesDataSourceFactory.isInitialized)
            moviesDataSourceFactory = MovieDataSourceFactory(movie)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        if (!this::moviesDataSourceFactory.isInitialized)
            moviesDataSourceFactory = MovieDataSourceFactory(movie)

        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}