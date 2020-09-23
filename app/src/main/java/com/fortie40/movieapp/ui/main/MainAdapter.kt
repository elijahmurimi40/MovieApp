package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.MovieResponseDiffCallback

class MainAdapter: ListAdapter<MovieResponse, MainViewHolder>(MovieResponseDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.createMainViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val movieResponse = getItem(position)
        holder.bind(movieResponse)
    }
}