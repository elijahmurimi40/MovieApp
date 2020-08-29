package com.fortie40.movieapp.ui.details.tabfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fortie40.movieapp.databinding.OverviewTabBinding

class TabOverview : Fragment() {
    private lateinit var overViewTabBinding: OverviewTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        overViewTabBinding = OverviewTabBinding.inflate(inflater)
        return overViewTabBinding.root
    }
}