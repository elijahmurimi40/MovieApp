package com.fortie40.movieapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.layoutmanagers.ItemDecorationMovieColumn
import com.fortie40.movieapp.layoutmanagers.MoviesStaggeredGridLayoutManager
import com.fortie40.movieapp.models.Movie
import com.fortie40.movieapp.ui.main.MainActivityAdapter

private lateinit var adapter: MainActivityAdapter

@BindingAdapter("setLayoutManager")
fun setLayoutManager(rv: RecyclerView, spanCount: Int) {
    val layoutManager = MoviesStaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
    rv.invalidateItemDecorations()
    rv.addItemDecoration(ItemDecorationMovieColumn(rv.context))
    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                rv.invalidateItemDecorations()
            }
        }
    })
    rv.layoutManager = layoutManager
}

@BindingAdapter("setAdapter")
fun setAdapter(rv: RecyclerView, data: PagedList<Movie>?) {
    if (!::adapter.isInitialized)
        adapter = MainActivityAdapter()

    rv.adapter = adapter
    data.let {
        adapter.submitList(it)
    }

    rv.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            rv.adapter = null
        }

        override fun onViewAttachedToWindow(v: View?) = Unit
    })
}

@BindingAdapter(value = ["listIsEmpty", "setVisibility"], requireAll = true)
fun setVisibility(view: View, listIsEmpty: Boolean, networkState: NetworkState?) {
    if (!::adapter.isInitialized)
        adapter = MainActivityAdapter()

    networkState.let {
        when (view) {
            is ProgressBar -> {
                if (listIsEmpty && it == NetworkState.LOADING)
                    view.visibility = View.VISIBLE
                else {
                    view.visibility = View.GONE
                }
            }
            else -> {
                if (listIsEmpty && it == NetworkState.ERROR)
                    view.visibility = View.VISIBLE
                else {
                    view.visibility = View.GONE
                }
            }
        }

        if (!listIsEmpty)
            adapter.setNetWorkState(it!!)
    }
}
