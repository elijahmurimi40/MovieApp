package com.fortie40.movieapp.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R

class MainViewHolder(viewW: View): RecyclerView.ViewHolder(viewW) {
    val titleVV = viewW.findViewById<TextView>(R.id.movie_title_items)
    val titleRV = viewW.findViewById<RecyclerView>(R.id.recycler_view_items)
}