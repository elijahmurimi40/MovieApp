package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.databinding.ItemHorizontalBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions

class ItemViewHolder private constructor(private val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createItemViewHolder(parent: ViewGroup): ItemViewHolder {
            val viewDataBinding = HelperFunctions.viewInflater(parent, R.layout.item_horizontal)
            return ItemViewHolder(viewDataBinding)
        }
    }

    private val cardView = (binding as ItemHorizontalBinding).cardItem

    fun bind(movie: Movie, width: Int = 0) {
        val itemWidth = (width / 3.33) - 9
        cardView.layoutParams.width = itemWidth.toInt()
        binding.setVariable(BR.movieItem, movie)
        binding.executePendingBindings()
    }
}