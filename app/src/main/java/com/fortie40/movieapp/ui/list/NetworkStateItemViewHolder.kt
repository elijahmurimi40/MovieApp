package com.fortie40.movieapp.ui.list

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.helperclasses.HelperFunctions

class NetworkStateItemViewHolder private constructor(private val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createNetworkStateItemViewHolder(parent: ViewGroup): NetworkStateItemViewHolder {
            val viewDataBinding = HelperFunctions.viewInflater(parent, R.layout.network_state_item)
            return NetworkStateItemViewHolder(viewDataBinding)
        }
    }

    fun bind(networkState: NetworkState?) {
        if (networkState != null) {
            binding.setVariable(BR.networkState, networkState)
            binding.executePendingBindings()
        }
    }
}