package com.fortie40.movieapp.ui.details.tabfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fortie40.movieapp.databinding.TrailersTabBinding
import com.fortie40.movieapp.ui.details.DetailsActivityViewModel

class TabTrailers : Fragment() {
    private lateinit var trailersTabBinding: TrailersTabBinding
    private val viewModel: DetailsActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        trailersTabBinding = TrailersTabBinding.inflate(inflater)
        return trailersTabBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.movieVideos.observe(viewLifecycleOwner, {
            println(it)
        })
    }
}