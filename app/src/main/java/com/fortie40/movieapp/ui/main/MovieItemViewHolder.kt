package com.fortie40.movieapp.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.models.Movie
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieItemViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun createMovieItemViewHolder(parent: ViewGroup): MovieItemViewHolder {
            val view = MainActivityAdapter.viewInflater(parent, R.layout.movie_list_item)
            return MovieItemViewHolder(view)
        }
    }

    fun bind(movie: Movie?) {
        itemView.movie_name.text = movie?.title
    }
}