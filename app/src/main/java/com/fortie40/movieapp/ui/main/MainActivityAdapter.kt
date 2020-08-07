package com.fortie40.movieapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.MOVIE_VIEW_TYPE
import com.fortie40.movieapp.NETWORK_VIEW_TYPE
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.models.Movie

class MainActivityAdapter : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    companion object {
        fun viewInflater(parent: ViewGroup, layout: Int): View {
            val layoutInflater = LayoutInflater.from(parent.context)
            return layoutInflater.inflate(layout, parent, false)
        }

        fun viewInflater2(parent: ViewGroup, layout: Int): ViewDataBinding {
            val layoutInflater = LayoutInflater.from(parent.context)
            return DataBindingUtil.inflate(layoutInflater, layout, parent, false)
        }
    }

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOVIE_VIEW_TYPE -> MovieItemViewHolder.createMovieItemViewHolder(parent)
            else -> NetworkStateItemViewHolder.createNetworkStateItemViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position))
        } else {
            val layoutParams = holder.itemView.layoutParams as
                    StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
            holder.itemView.layoutParams = layoutParams
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
            if (hadExtraRow) {                          // hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount()) // remove the progressbar at the end
            } else {                                    // hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())// add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { // hasExtraRow is true and hadExtra is true
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}