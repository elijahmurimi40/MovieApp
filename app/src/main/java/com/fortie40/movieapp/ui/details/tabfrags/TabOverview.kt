package com.fortie40.movieapp.ui.details.tabfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fortie40.movieapp.databinding.OverviewTabBinding
import com.fortie40.movieapp.ui.details.DetailsActivityViewModel

class TabOverview : Fragment() {
    private lateinit var overViewTabBinding: OverviewTabBinding
    private val viewModel: DetailsActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        overViewTabBinding = OverviewTabBinding.inflate(inflater)
        return overViewTabBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        overViewTabBinding.apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.viewModel = this@TabOverview.viewModel
        }
    }
}