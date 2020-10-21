package com.fortie40.movieapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.MOVIE_VIEW_TYPE
import com.fortie40.movieapp.NETWORK_VIEW_TYPE
import com.fortie40.movieapp.SCROLL_POSITION_HORIZONTAL
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.helperclasses.MovieLinearLayoutManager
import com.fortie40.movieapp.helperclasses.MovieResponseDiffCallback
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.ui.list.NetworkStateItemViewHolder

class MainAdapter(private val context: Context):
    ListAdapter<MovieResponse, RecyclerView.ViewHolder>(MovieResponseDiffCallback()){
    private var networkState: NetworkState? = null
    private var scrollState = HashMap<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MOVIE_VIEW_TYPE -> MainViewHolder.createMainViewHolder(parent)
            else -> NetworkStateItemViewHolder.createNetworkStateItemViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            val movieResponse = getItem(position)
            (holder as MainViewHolder).bind(movieResponse, context as IClickListener)

            val lm = holder.rv.layoutManager as MovieLinearLayoutManager
            val layoutPosition = scrollState[holder.layoutPosition]
            if (layoutPosition != null)
                lm.scrollToPositionWithOffset(layoutPosition, 22)
            holder.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val positionIndex = lm.findFirstCompletelyVisibleItemPosition()
                        scrollState[holder.layoutPosition] = positionIndex
                    }
                }
            })
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetWorkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if(hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        @Suppress("UNCHECKED_CAST")
        val p = savedInstanceState.getSerializable(SCROLL_POSITION_HORIZONTAL) as HashMap<Int, Int>
        scrollState = p
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(SCROLL_POSITION_HORIZONTAL, scrollState)
    }

    fun resetScrollState() {
        scrollState.clear()
    }
}