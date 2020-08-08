package com.fortie40.movieapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fortie40.movieapp.R
import com.fortie40.movieapp.helperclasses.NetworkState

@BindingAdapter("setProgressBarItemVisibility")
fun setProgressBarItemVisibility(pb: ProgressBar, networkState: NetworkState) {
    if (networkState == NetworkState.LOADING)
        pb.visibility = View.VISIBLE
    else
        pb.visibility = View.GONE
}

@BindingAdapter("setErrorMessageItemVisibility")
fun setErrorMessageItemVisibility(tv: TextView, networkState: NetworkState) {
    when (networkState) {
        NetworkState.ERROR -> {
            tv.visibility = View.VISIBLE
            tv.text = tv.context.getString(R.string.something_went_wrong)
        }
        NetworkState.END_OF_LIST -> {
            tv.visibility = View.VISIBLE
            tv.text = tv.context.getString(R.string.the_end)
        }
        else -> tv.visibility = View.GONE
    }
}