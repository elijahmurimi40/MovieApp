package com.fortie40.movieapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.helperclasses.NetworkState

@BindingAdapter("setVisibilityDetails")
fun setVisibilityDetails(view: View, networkState: NetworkState?) {
    networkState.let {
        when (view) {
            is ProgressBar -> view.visibility = if (it == NetworkState.LOADING || it == null) View.VISIBLE else View.GONE
            else -> view.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        }
    }
}

@BindingAdapter("setUpDetails")
fun setUpDetails(view: View, movieDetails: MovieDetails?) {
    movieDetails.let {
        when (view) {
            is ImageView -> {
                if (it != null)
                    setImage(view, it.posterPath)
            }
        }
    }
}

@BindingAdapter("setTextDateRunTime")
fun setTextDateRunTime(tv: TextView, movieDetails: MovieDetails?) {
    if (movieDetails != null) {
        val year = movieDetails.releaseDate.substring(0, 4)

        val hour = movieDetails.runtime.div(60)
        val minute = movieDetails.runtime.minus(hour.times(60))
        val time = tv.context.getString(R.string.h_min, hour, minute)

        tv.text = tv.context.getString(R.string.date_time, year, time)
    }
}