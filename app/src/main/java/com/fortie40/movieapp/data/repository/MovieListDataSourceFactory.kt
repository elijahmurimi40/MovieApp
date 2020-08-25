package com.fortie40.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import retrofit2.Call

class MovieListDataSourceFactory(private val movie: (Int) -> Call<MovieResponse>)
    : DataSource.Factory<Int, Movie>() {

    private val _moviesLiveDataSource = MutableLiveData<MovieListDataSource>()
    val moviesLiveListDataSource: LiveData<MovieListDataSource> = _moviesLiveDataSource

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieListDataSource(movie)
        _moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}