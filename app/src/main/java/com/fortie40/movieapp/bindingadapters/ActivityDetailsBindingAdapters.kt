package com.fortie40.movieapp.bindingadapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.databinding.BindingAdapter
import com.fortie40.movieapp.POSTER_BASE_URL
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.helperclasses.NetworkState
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import jp.wasabeef.picasso.transformations.BlurTransformation

private fun setImageDetails(iv: ImageView, poster_path: String?) {
    val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            iv.scaleType = ImageView.ScaleType.CENTER
            iv.setImageResource(R.drawable.place_holder)
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            iv.scaleType = ImageView.ScaleType.CENTER
            iv.setImageResource(R.drawable.error)
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            iv.doOnPreDraw {
                if (bitmap != null) {
                    iv.requestLayout()
                    iv.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                }
            }
            iv.scaleType = ImageView.ScaleType.FIT_XY
            iv.setImageBitmap(bitmap)
        }
    }
    val transformation = BlurTransformation(iv.context)

    iv.tag = target

    Picasso.get()
        .load(POSTER_BASE_URL + poster_path)
        .transform(transformation)
        .into(target)
}

@BindingAdapter("setVisibilityDetails")
fun setVisibilityDetails(view: View, networkState: NetworkState?) {
    networkState.let {
        when (view) {
            is ProgressBar -> view.visibility =
                if (it == NetworkState.LOADING || it == null) View.VISIBLE else View.GONE
            else -> view.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        }
    }
}

@BindingAdapter("setUpDetails")
fun setUpDetails(iv: ImageView, movieDetails: MovieDetails?) {
    movieDetails.let {
        if (it != null)
            setImageDetails(iv, it.posterPath)
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