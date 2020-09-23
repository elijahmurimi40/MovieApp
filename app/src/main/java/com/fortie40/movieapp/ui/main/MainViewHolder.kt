package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.HelperFunctions

class MainViewHolder private constructor(private val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createMainViewHolder(parent: ViewGroup): MainViewHolder {
            val viewDataBinding = HelperFunctions.viewInflater(parent, R.layout.item_vertical)
            return MainViewHolder(viewDataBinding)
        }
    }

    fun bind(movieResponse: MovieResponse) {
        binding.setVariable(BR.movieResponse, movieResponse)
        binding.executePendingBindings()
    }
}