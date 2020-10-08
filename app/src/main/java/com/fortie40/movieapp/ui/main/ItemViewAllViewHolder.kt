package com.fortie40.movieapp.ui.main

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.databinding.ViewAllBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.interfaces.IClickListener

class ItemViewAllViewHolder private constructor(val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createItemViewAllViewHolder(parent: ViewGroup): ItemViewAllViewHolder {
            val viewBinding = HelperFunctions.viewInflater(parent, R.layout.view_all)
            return ItemViewAllViewHolder(viewBinding)
        }
    }

    private val constraintLayout = (binding as ViewAllBinding).viewAllRoot

    fun bind(width: Int = 0, iClickListener: IClickListener, title: String) {
        val itemWidth = (width / 3.33) - 9
        constraintLayout.layoutParams.width = itemWidth.toInt()
        binding.setVariable(BR.iClickListener, iClickListener)
        binding.setVariable(BR.title, title)
        binding.executePendingBindings()
    }
}