package com.fortie40.movieapp.ui.details.tabfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fortie40.movieapp.databinding.TrailersTabBinding

class TabTrailers : Fragment() {
    private lateinit var trailersTabBinding: TrailersTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        trailersTabBinding = TrailersTabBinding.inflate(inflater)
        return trailersTabBinding.root
    }
}