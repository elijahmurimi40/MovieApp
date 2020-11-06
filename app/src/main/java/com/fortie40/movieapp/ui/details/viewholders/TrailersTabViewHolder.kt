package com.fortie40.movieapp.ui.details.viewholders

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.databinding.YoutubeVideosBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class TrailersTabViewHolder private constructor(private val binding: ViewDataBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createTrailersTabViewHolder(parent: ViewGroup): TrailersTabViewHolder {
            val viewDataBinding = HelperFunctions.viewInflater(parent, R.layout.youtube_videos)
            return TrailersTabViewHolder(viewDataBinding)
        }
    }

    private val youTubePlayerView = (binding as YoutubeVideosBinding).youtubePlayerView
    private val youTubeOverlay = (binding as YoutubeVideosBinding).youtubeOverlay

    fun bind(video: Video, youTubePlayerView: (YouTubePlayerView) -> Unit, openTrailer: (String, String) -> Unit) {
        youTubePlayerView(this.youTubePlayerView)
        youTubeOverlay.setOnClickListener {
            openTrailer(video.key, video.name)
        }
        binding.setVariable(BR.video, video)
        binding.executePendingBindings()
    }
}