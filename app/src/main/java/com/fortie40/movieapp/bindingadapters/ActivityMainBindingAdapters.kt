package com.fortie40.movieapp.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.layoutmanagers.MoviesStaggeredGridLayoutManager
import com.fortie40.movieapp.models.Movie
import com.fortie40.movieapp.ui.main.MainActivityAdapter

@BindingAdapter("setLayoutManager")
fun setLayoutManager(rv: RecyclerView, spanCount: Int) {
    val layoutManager = MoviesStaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    rv.layoutManager = layoutManager
}

@BindingAdapter("setAdapter")
fun setAdapter(rv: RecyclerView, data: PagedList<Movie>?) {
    val adapter = MainActivityAdapter()
    rv.adapter = adapter
    data.let {
        adapter.submitList(it)
    }
}