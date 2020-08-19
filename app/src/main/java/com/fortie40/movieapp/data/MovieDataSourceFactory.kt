package com.fortie40.movieapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fortie40.movieapp.models.Movie
import com.fortie40.movieapp.models.MovieResponse
import retrofit2.Call

class MovieDataSourceFactory(private val movie: (Int) -> Call<MovieResponse>)
    : DataSource.Factory<Int, Movie>() {

    private val _moviesLiveDataSource = MutableLiveData<MovieDataSource>()
    val moviesLiveDataSource: LiveData<MovieDataSource> = _moviesLiveDataSource

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(movie)
        _moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}