package com.fortie40.movieapp.bindingadapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fortie40.movieapp.POSTER_BASE_URL
import com.fortie40.movieapp.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


@BindingAdapter("setImage")
fun setImage(iv: ImageView, poster_path: String?) {
    val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            val imageViewWidth = iv.width
            println(imageViewWidth)
            iv.scaleType = ImageView.ScaleType.CENTER
            iv.setImageResource(R.drawable.place_holder)
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            iv.scaleType = ImageView.ScaleType.FIT_XY
            iv.setImageResource(R.drawable.error)
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            //iv.scaleType = ImageView.ScaleType.FIT_XY
            iv.setImageBitmap(bitmap)
        }
    }

    iv.tag = target

    Picasso.get()
        .load(POSTER_BASE_URL + poster_path)
        .into(target)
}