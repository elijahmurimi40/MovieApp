package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.MOVIE_VIEW_HORIZONTAL
import com.fortie40.movieapp.VIEW_ALL_HORIZONTAL
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.helperclasses.MovieDiffCallback

class ItemAdapter(private val movies: List<Movie>) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_VIEW_HORIZONTAL -> ItemViewHolder.createItemViewHolder(parent)
            else -> ItemViewAllViewHolder.createItemViewAllViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != movies.size) {
            val movie = getItem(position)
            (holder as ItemViewHolder).bind(movie)
        }
    }

    override fun getItemCount(): Int {
        return movies.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movies.size) VIEW_ALL_HORIZONTAL else MOVIE_VIEW_HORIZONTAL
    }
}