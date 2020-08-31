package com.fortie40.movieapp.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.MOVIE_ID
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.databinding.ActivityDetailsBinding
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.ui.details.tabfrags.PagerAdapter
import com.google.android.material.tabs.TabLayout

class DetailsActivity : AppCompatActivity() {
    private lateinit var activityDetailsBinding: ActivityDetailsBinding

    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        activityDetailsBinding.toolbar.setOnClickListener {
            onBackPressed()
        }

        setUpTabLayout()

        val movieId = intent.getIntExtra(MOVIE_ID, 605116)

        movieDetailsRepository = MovieDetailsRepository(RetrofitCallback::tMDbMovies)
        viewModelFactory = ViewModelFactory(movieDetailsRepository, movieId)
        val viewModel: DetailsActivityViewModel by viewModels {viewModelFactory}

        activityDetailsBinding.apply {
            this.lifecycleOwner = this@DetailsActivity
            this.viewModel = viewModel
        }
    }

    private fun setUpTabLayout() {
        val tabLayout = activityDetailsBinding.tabLayout
        val pager = activityDetailsBinding.pager

        tabLayout.addTab(tabLayout.newTab().setText(R.string.overview))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.trailers))

        val pagerAdapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        pager.adapter = pagerAdapter

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pager.currentItem = tab!!.position
            }
        })
    }
}