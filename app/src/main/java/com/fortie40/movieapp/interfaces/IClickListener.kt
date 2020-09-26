package com.fortie40.movieapp.interfaces

interface IClickListener {
    fun onMovieClick(id: Int) {}

    fun onMoreClick(title: String) {}
}