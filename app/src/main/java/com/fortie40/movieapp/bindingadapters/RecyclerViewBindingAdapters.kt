package com.fortie40.movieapp.bindingadapters

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.helperclasses.*
import com.fortie40.movieapp.ui.list.ListActivityAdapter
import com.fortie40.movieapp.ui.main.ItemAdapter

private var adapter: ListActivityAdapter? = null

private fun addOnScrollListener(rv: RecyclerView) {
    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                rv.invalidateItemDecorations()
            }
        }
    })
}

private fun setUpAdapter(context: Context) {
    if (adapter == null)
        adapter = ListActivityAdapter(context)
}

private fun tearDownAdapter(rv: RecyclerView) {
    rv.adapter = null
    adapter = null
}

@BindingAdapter("setLayoutManager")
fun setLayoutManager(rv: RecyclerView, spanCount: Int) {
    val layoutManager = MovieListStaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
    rv.invalidateItemDecorations()
    rv.addItemDecoration(ItemDecorationMovieListColumns(rv.context, spanCount))
    addOnScrollListener(rv)
    rv.layoutManager = layoutManager
}

@BindingAdapter("setAdapter")
fun setAdapter(rv: RecyclerView, data: PagedList<Movie>?) {
    setUpAdapter(rv.context)
    data.let {
        adapter!!.submitList(it)
    }

    rv.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            tearDownAdapter(rv)
        }

        override fun onViewAttachedToWindow(v: View?) {
            setUpAdapter(rv.context)
            rv.adapter = adapter
        }
    })
}

@BindingAdapter(value = ["listIsEmpty", "setVisibility"], requireAll = true)
fun setVisibility(view: View, listIsEmpty: Boolean, networkState: NetworkState?) {
    setUpAdapter(view.context)
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
            adapter!!.setNetWorkState(it!!)
    }
}

@BindingAdapter("setUpLinearLayout")
fun setUpLinearLayout(rv: RecyclerView, type: Int) {
    rv.setHasFixedSize(true)
    rv.invalidateItemDecorations()
    when (type) {
        1 -> {
            rv.addItemDecoration(ItemDecorationMainList(rv.context))
            rv.layoutManager = MovieLinearLayoutManager(rv.context, LinearLayoutManager.VERTICAL, false)
        }
        else -> {
            rv.addItemDecoration(ItemDecorationHorizontal(rv.context))
            rv.layoutManager = MovieLinearLayoutManager(rv.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    addOnScrollListener(rv)
}

@BindingAdapter("setData")
fun setData(rv: RecyclerView, movies: List<Movie>) {
    val itemAdapter = ItemAdapter(movies)
    rv.adapter = itemAdapter
    itemAdapter.submitList(movies)
}

@BindingAdapter(value = ["listIsEmptyM", "setVisibilityM", "setIdM"], requireAll = true)
fun setVisibilityMain(view: View, listIsEmpty: Boolean, networkState: NetworkState?, id: Int) {
    networkState.let {
        when (view) {
            is ProgressBar -> {
                if (listIsEmpty && it == NetworkState.LOADING && id <= 1)
                    view.visibility = View.VISIBLE
                else {
                    view.visibility = View.GONE
                }
            }
            else -> {
                if (listIsEmpty && it == NetworkState.ERROR && id <= 1)
                    view.visibility = View.VISIBLE
                else {
                    view.visibility = View.GONE
                }
            }
        }
    }
}
