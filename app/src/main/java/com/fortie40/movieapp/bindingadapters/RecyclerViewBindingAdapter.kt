package com.fortie40.movieapp.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.layoutmanagers.MoviesStaggeredGridLayoutManager

@BindingAdapter("setLayoutManager")
fun setLayoutManager(rv: RecyclerView, spanCount: Int) {
    val layoutManager = MoviesStaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    rv.layoutManager = layoutManager
}