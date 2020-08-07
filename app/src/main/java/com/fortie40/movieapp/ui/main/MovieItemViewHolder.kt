package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.models.Movie

class MovieItemViewHolder private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createMovieItemViewHolder(parent: ViewGroup): MovieItemViewHolder {
            val viewDataBinding = MainActivityAdapter.viewInflater2(parent, R.layout.movie_list_item)
            return MovieItemViewHolder(viewDataBinding)
        }
    }

    fun bind(movie: Movie?) {
        binding.setVariable(BR.movie, movie)
        binding.executePendingBindings()
    }
}