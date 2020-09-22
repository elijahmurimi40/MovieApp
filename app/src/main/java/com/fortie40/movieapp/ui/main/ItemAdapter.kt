package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.helperclasses.MovieDiffCallback

class ItemAdapter: ListAdapter<Movie, ItemViewHolder>(MovieDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.createMovieItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }
}