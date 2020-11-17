package com.fortie40.movieapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fortie40.movieapp.POST_PER_PAGE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.roomdatabase.MovieAppRoomDatabase
import com.fortie40.movieapp.data.roomdatabase.MovieAppDao
import retrofit2.Call

class MovieListRepository(moviesPage: (Int) -> Call<MovieResponse>, application: Application) {
    private val movieListDataSourceFactory: MovieListDataSourceFactory = MovieListDataSourceFactory(moviesPage)
    private lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private var dao: MovieAppDao

    init {
        val database = MovieAppRoomDatabase(application)
        dao = database.movieAppDao()
    }

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

    fun getMovieResponse(): LiveData<MovieResponse> {
        return Transformations.switchMap(
            movieListDataSourceFactory.movieLiveListDataSource, MovieListDataSource::movieResponse
        )
    }

    fun retry() {
        movieListDataSourceFactory.retry()
    }

    suspend fun saveMovieResponse(movieResponse: MovieResponse) {
        dao.saveMovieResponse(movieResponse)
    }

    suspend fun getMovieResponseByPage(page: Int): MovieResponse {
        return dao.getMovieResponseByPage(page)
    }
}