package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.Movie

class ItemViewHolder private constructor(private val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createMovieItemViewHolder(parent: ViewGroup): ItemViewHolder {
            val viewDataBinding = ItemAdapter.viewInflater(parent, R.layout.item_horizontal)
            return ItemViewHolder(viewDataBinding)
        }
    }

    fun bind(movie: Movie) {
        binding.setVariable(BR.movieItem, movie)
        binding.executePendingBindings()
    }
}