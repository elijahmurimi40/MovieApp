package com.fortie40.movieapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.helperclasses.NetworkState
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

@BindingAdapter(value = ["listIsEmptyP", "setProgressBarPopularVisibility"], requireAll = true)
fun setProgressBarPopularVisibility(pb: ProgressBar, listIsEmptyP: Boolean, networkState: NetworkState?) {
    if (listIsEmptyP && networkState == NetworkState.LOADING)
        pb.visibility = View.VISIBLE
    else {
        pb.visibility = View.GONE
    }
}

@BindingAdapter(value = ["listIsEmptyT", "setTextErrorPopular"], requireAll = true)
fun setTextErrorPopular(tv: TextView, listIsEmptyT: Boolean, networkState: NetworkState?) {
    if (listIsEmptyT && networkState == NetworkState.ERROR)
        tv.visibility = View.VISIBLE
    else {
        tv.visibility = View.GONE
    }
}
