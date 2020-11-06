package com.fortie40.movieapp.ui.details.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.helperclasses.VideoDiffCallback
import com.fortie40.movieapp.ui.details.viewholders.TrailersTabViewHolder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class TrailersTabAdapter(private val youTubePlayerView: (YouTubePlayerView) -> Unit,
                         private val openTrailer: (String, String) -> Unit)
    : ListAdapter<Video, TrailersTabViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailersTabViewHolder {
        return TrailersTabViewHolder.createTrailersTabViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrailersTabViewHolder, position: Int) {
        val video = getItem(position)
        if (!video.key.isBlank())
            holder.bind(video, youTubePlayerView, openTrailer)
    }
}