package com.fortie40.movieapp.ui.details.tabfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.databinding.TrailersTabBinding
import com.fortie40.movieapp.helperclasses.MovieLinearLayoutManager
import com.fortie40.movieapp.ui.details.DetailsActivityViewModel
import com.fortie40.movieapp.ui.details.adapters.TrailersTabAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class TabTrailers : Fragment() {
    private lateinit var trailersTabBinding: TrailersTabBinding
    private lateinit var adapter: TrailersTabAdapter
    private lateinit var rv: RecyclerView
    private lateinit var lm: MovieLinearLayoutManager

    private val viewModel: DetailsActivityViewModel by activityViewModels()

    private val youTubePlayerView = { youTubePlayerView: YouTubePlayerView ->
        lifecycle.addObserver(youTubePlayerView)
    }

    private val openTrailer = {key: String ->
        println(key)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        trailersTabBinding = TrailersTabBinding.inflate(inflater)
        rv = trailersTabBinding.youtubePlayerRecyclerView
        lm = MovieLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = TrailersTabAdapter(youTubePlayerView, openTrailer)
        return trailersTabBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv.layoutManager = lm
        rv.adapter = adapter
        viewModel.movieVideos.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }
}