package com.fortie40.movieapp.helperclasses

import androidx.recyclerview.widget.DiffUtil
import com.fortie40.movieapp.data.models.Video

class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }
}