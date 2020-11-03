package com.fortie40.movieapp.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.fortie40.movieapp.*
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.fortie40.movieapp.data.models.MovieDetails
import com.fortie40.movieapp.data.models.Video
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.databinding.ActivityDetailsBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.interfaces.INetworkStateReceiver
import com.fortie40.movieapp.ui.details.tabfrags.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlin.properties.Delegates

class DetailsActivity : AppCompatActivity(), IClickListener, INetworkStateReceiver {
    private lateinit var activityDetailsBinding: ActivityDetailsBinding

    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: DetailsActivityViewModel
    private lateinit var pager: ViewPager

    private var movieId by Delegates.notNull<Int>()

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

        movieId = intent.getIntExtra(MOVIE_ID, 605116)

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
            this.iClickListener = this@DetailsActivity
        }

        activityDetailsBinding.swipeToRefresh.setOnRefreshListener {
            getMovieDetails()
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

    override fun onResume() {
        super.onResume()
        HelperFunctions.registerInternetReceiver(this)
        if (!NetworkStateReceiver.isNetworkAvailable(this))
            networkNotAvailable()
    }

    override fun onPause() {
        HelperFunctions.unregisterInternetReceiver(this)
        super.onPause()
    }

    override fun onBackPressed() {
        if (pager.currentItem != DEFAULT_PAGE)
            pager.currentItem = DEFAULT_PAGE
        else
            super.onBackPressed()
    }

    override fun onRetryClick() {
        getMovieDetails()
    }

    override fun networkAvailable() {
        val textView = activityDetailsBinding.networkReceiver.status
        val actionBar = activityDetailsBinding.appBarLayout

        HelperFunctions.networkAvailable(textView, actionBar)

        if (activityDetailsBinding.textError.visibility == View.VISIBLE) {
            getMovieDetails()
        }
    }

    override fun networkNotAvailable() {
        val textView = activityDetailsBinding.networkReceiver.status
        val actionBar = activityDetailsBinding.appBarLayout

        HelperFunctions.networkNotAvailable(textView, actionBar)
    }

    private fun getMovieDetails() {
        MovieDetailsRepository.load = true
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        viewModel.refresh(movieId as Integer)
        activityDetailsBinding.swipeToRefresh.isRefreshing = false
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

    @SuppressLint("ClickableViewAccessibility")
    fun enableDisableSwipeRefresh(view: View) {
        view.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> { activityDetailsBinding.swipeToRefresh.isEnabled = false; true }
                MotionEvent.ACTION_UP -> { activityDetailsBinding.swipeToRefresh.isEnabled = true; true }
                else -> false
            }
        }
    }

    fun swipeToRefresh(): SwipeRefreshLayout {
        return activityDetailsBinding.swipeToRefresh
    }
}