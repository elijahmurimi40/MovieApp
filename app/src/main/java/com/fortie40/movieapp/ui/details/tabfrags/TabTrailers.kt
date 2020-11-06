package com.fortie40.movieapp.ui.details.tabfrags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.MOVIE_KEY
import com.fortie40.movieapp.MOVIE_TITLE
import com.fortie40.movieapp.OFFSET_TRAILERS
import com.fortie40.movieapp.POSITION_INDEX_TRAILERS
import com.fortie40.movieapp.databinding.TrailersTabBinding
import com.fortie40.movieapp.helperclasses.MovieLinearLayoutManager
import com.fortie40.movieapp.ui.details.DetailsActivity
import com.fortie40.movieapp.ui.details.DetailsActivityViewModel
import com.fortie40.movieapp.ui.details.adapters.TrailersTabAdapter
import com.fortie40.movieapp.ui.trailer.TrailerActivity
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

    private val openTrailer = { key: String, name: String ->
        val intent = Intent(requireActivity(), TrailerActivity::class.java)
        intent.putExtra(MOVIE_KEY, key)
        intent.putExtra(MOVIE_TITLE, name)
        startActivity(intent)
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

        savedInstanceState?.run {
            val positionIndex = getInt(POSITION_INDEX_TRAILERS, -1)
            val offset = getInt(OFFSET_TRAILERS)
            if (positionIndex != -1) {
                lm.scrollToPositionWithOffset(positionIndex, offset)
            }
        }

        rv.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when(e.action) {
                    MotionEvent.ACTION_DOWN -> (requireActivity() as DetailsActivity).swipeToRefresh().isEnabled = false
                    MotionEvent.ACTION_UP -> (requireActivity() as DetailsActivity).swipeToRefresh().isEnabled = true
                }
                return false
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) = Unit
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (viewModel.movieVideos.value != null && viewModel.movieVideos.value!!.isNotEmpty()) {
            val startView = rv.getChildAt(0)
            val positionIndex = lm.findFirstVisibleItemPosition()
            val offSet = if (startView == null) 0 else startView.top - rv.top

            outState.run {
                putInt(POSITION_INDEX_TRAILERS, positionIndex)
                putInt(OFFSET_TRAILERS, offSet)
            }
        }
        super.onSaveInstanceState(outState)
    }
}