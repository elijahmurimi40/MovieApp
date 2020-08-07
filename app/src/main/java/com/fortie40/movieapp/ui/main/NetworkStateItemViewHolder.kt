package com.fortie40.movieapp.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.helperclasses.NetworkState
import kotlinx.android.synthetic.main.network_state_item.view.*

class NetworkStateItemViewHolder private constructor(private val view: View):
    RecyclerView.ViewHolder(view) {

    companion object {
        fun createNetworkStateItemViewHolder(parent: ViewGroup): NetworkStateItemViewHolder {
            val view = MainActivityAdapter.viewInflater(parent, R.layout.network_state_item)
            return NetworkStateItemViewHolder(view)
        }
    }

    fun bind(networkState: NetworkState?) {
        if (networkState != null && networkState == NetworkState.LOADING) {
            itemView.progress_bar_item.visibility = View.VISIBLE
        } else {
            itemView.progress_bar_item.visibility = View.GONE
        }

        if (networkState != null && networkState == NetworkState.ERROR) {
            itemView.error_msg_item.visibility = View.VISIBLE
            itemView.error_msg_item.text = view.context.getString(R.string.something_went_wrong)
        } else if (networkState != null && networkState == NetworkState.END_OF_LIST) {
            itemView.error_msg_item.visibility = View.VISIBLE
            itemView.error_msg_item.text = view.context.getString(R.string.the_end)
        } else {
            itemView.error_msg_item.visibility = View.GONE
        }
    }
}