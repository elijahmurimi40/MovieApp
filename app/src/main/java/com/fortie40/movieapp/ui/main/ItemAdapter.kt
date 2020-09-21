package com.fortie40.movieapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fortie40.movieapp.data.models.Movie

class ItemAdapter: ListAdapter<Movie, ItemViewHolder>(MovieDiffCallback()) {
    companion object {
        fun viewInflater(parent: ViewGroup, layout: Int): ViewDataBinding {
            val layoutInflater = LayoutInflater.from(parent.context)
            return DataBindingUtil.inflate(layoutInflater, layout, parent, false)
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.createMovieItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }
}