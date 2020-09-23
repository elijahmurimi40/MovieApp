package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.helperclasses.HelperFunctions

class ItemViewAllViewHolder private constructor(val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createItemViewAllViewHolder(parent: ViewGroup): ItemViewAllViewHolder {
            val viewBinding = HelperFunctions.viewInflater(parent, R.layout.view_all)
            return ItemViewAllViewHolder(viewBinding)
        }
    }
}