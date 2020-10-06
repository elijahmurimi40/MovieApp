package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.databinding.ItemVerticalBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.interfaces.IClickListener

class MainViewHolder private constructor(private val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createMainViewHolder(parent: ViewGroup): MainViewHolder {
            val viewDataBinding = HelperFunctions.viewInflater(parent, R.layout.item_vertical)
            return MainViewHolder(viewDataBinding)
        }
    }

    val rv = (binding as ItemVerticalBinding).recyclerViewItems

    fun bind(movieResponse: MovieResponse, iClickListener: IClickListener) {
        binding.setVariable(BR.movieResponse, movieResponse)
        binding.setVariable(BR.iClickListener, iClickListener)
        binding.executePendingBindings()
    }
}