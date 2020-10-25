package com.fortie40.movieapp.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.fortie40.movieapp.*
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.Video
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
    private lateinit var viewModel: DetailsActivityViewModel
    private lateinit var pager: ViewPager

    private var movieDetails: MovieDetails? = MovieDetails()
    private var videos: MutableList<Video> = arrayListOf()

    companion object {
        const val DEFAULT_PAGE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        activityDetailsBinding.toolbar.setOnClickListener {
            onBackPressed()
        }

        setUpTabLayout(savedInstanceState)

        val movieId = intent.getIntExtra(MOVIE_ID, 605116)

        movieDetailsRepository = MovieDetailsRepository(RetrofitCallback::tMDbMovies)
        viewModelFactory = ViewModelFactory(movieDetailsRepository, movieId)
       // val viewModel: DetailsActivityViewModel by viewModels {viewModelFactory}
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsActivityViewModel::class.java)

        savedInstanceState?.run {
            val mD = getParcelable<MovieDetails>(MOVIE_DETAILS)
            val v = getParcelableArrayList<Video>(VIDEO_LIST)
            if (mD != null && v != null) {
                MovieDetailsRepository.load = false
                viewModel.movieDetails.value = mD
                viewModel.movieVideos.value = v
            }
        }

        activityDetailsBinding.apply {
            this.lifecycleOwner = this@DetailsActivity
            this.viewModel = this@DetailsActivity.viewModel
        }

        activityDetailsBinding.swipeToRefresh.setOnRefreshListener {
            MovieDetailsRepository.load = true
            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            viewModel.refresh(movieId as Integer)
            activityDetailsBinding.swipeToRefresh.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(CURRENT_PAGE, pager.currentItem)
        }

        val mD = viewModel.movieDetails.value
        val v = viewModel.movieVideos.value
        if (mD != null && v !=null) {
            outState.run {
                putParcelable(MOVIE_DETAILS, mD)
                putParcelableArrayList(VIDEO_LIST, ArrayList(v))
            }
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (pager.currentItem != DEFAULT_PAGE)
            pager.currentItem = DEFAULT_PAGE
        else
            super.onBackPressed()
    }

    private fun setUpTabLayout(savedInstanceState: Bundle?) {
        val tabLayout = activityDetailsBinding.tabLayout
        pager = activityDetailsBinding.pager

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

        if (savedInstanceState == null)
            pager.currentItem = DEFAULT_PAGE

        savedInstanceState?.run {
            val page = getInt(CURRENT_PAGE)
            pager.currentItem = page
        }
    }
}