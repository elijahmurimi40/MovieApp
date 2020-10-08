package com.fortie40.movieapp.ui.main

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.MOVIE_VIEW_HORIZONTAL
import com.fortie40.movieapp.VIEW_ALL_HORIZONTAL
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.helperclasses.MovieDiffCallback
import com.fortie40.movieapp.interfaces.IClickListener

class ItemAdapter(private val movies: List<Movie>, private val context: Context) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_VIEW_HORIZONTAL -> {
                val displayMetrics = DisplayMetrics()
                (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                screenWidth = displayMetrics.widthPixels
                ItemViewHolder.createItemViewHolder(parent)
            }
            else -> ItemViewAllViewHolder.createItemViewAllViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != movies.size) {
            val movie = getItem(position)
            (holder as ItemViewHolder).bind(movie, screenWidth, context as IClickListener)
        }
    }

    override fun getItemCount(): Int {
        return movies.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movies.size) VIEW_ALL_HORIZONTAL else MOVIE_VIEW_HORIZONTAL
    }
}