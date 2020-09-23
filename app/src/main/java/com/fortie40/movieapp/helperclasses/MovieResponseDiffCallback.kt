package com.fortie40.movieapp.helperclasses

import androidx.recyclerview.widget.DiffUtil
import com.fortie40.movieapp.data.models.MovieResponse

class MovieResponseDiffCallback: DiffUtil.ItemCallback<MovieResponse>() {
    override fun areItemsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieResponse, newItem: MovieResponse): Boolean {
        return oldItem == newItem
    }
}