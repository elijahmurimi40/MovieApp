package com.fortie40.movieapp.ui.list

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.helperclasses.HelperFunctions

class MovieItemViewHolder private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createMovieItemViewHolder(parent: ViewGroup): MovieItemViewHolder {
            val viewDataBinding = HelperFunctions.viewInflater(parent, R.layout.movie_list_item)
            return MovieItemViewHolder(viewDataBinding)
        }
    }

    fun bind(movie: Movie?, iClickListener: IClickListener) {
        binding.setVariable(BR.movie, movie)
        binding.setVariable(BR.iClickListener, iClickListener)
        binding.executePendingBindings()
    }
}