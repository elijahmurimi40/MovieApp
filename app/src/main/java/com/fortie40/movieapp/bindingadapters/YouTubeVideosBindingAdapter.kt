package com.fortie40.movieapp.bindingadapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fortie40.movieapp.R
import com.fortie40.movieapp.YOUTUBE_THUMBNAILS
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

@BindingAdapter("setVideoId")
fun setVideoId(iv: ImageView, videoId: String?) {
    val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) = Unit
        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            iv.scaleType = ImageView.ScaleType.FIT_XY
            iv.setImageResource(R.drawable.youtube_error)
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            iv.scaleType = ImageView.ScaleType.FIT_XY
            iv.setImageBitmap(bitmap)
        }
    }

    iv.tag = target

    Picasso.get()
        .load("$YOUTUBE_THUMBNAILS$videoId/mqdefault.jpg")
        .into(target)
}