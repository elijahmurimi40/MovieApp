package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.roomdatabase.MovieAppDao
import retrofit2.Call

class MovieListDataSourceFactory(
    private val moviesPage: (Int) -> Call<MovieResponse>,
    private val dao: MovieAppDao
) : DataSource.Factory<Int, Movie>() {

    private val _movieLiveListDataSource = MutableLiveData<MovieListDataSource>()
    val movieLiveListDataSource: LiveData<MovieListDataSource> = _movieLiveListDataSource

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieListDataSource(moviesPage, dao)
        _movieLiveListDataSource.postValue(movieDataSource)
        return movieDataSource
    }

    fun retry() {
        movieLiveListDataSource.value?.retry()
    }
}