package com.fortie40.movieapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.fortie40.movieapp.helperclasses.NetworkState

@BindingAdapter("setVisibility")
fun setVisibility(view: View, networkState: NetworkState?) {
    networkState.let {
        when (view) {
            is ProgressBar -> view.visibility = if (it == NetworkState.LOADING || it == null) View.VISIBLE else View.GONE
            else -> view.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        }
    }
}