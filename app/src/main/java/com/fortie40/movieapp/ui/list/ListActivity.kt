package com.fortie40.movieapp.ui.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fortie40.movieapp.*
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.data.repository.MovieListRepository
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.databinding.ActivityListBinding
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.helperclasses.PreferenceHelper.set
import com.fortie40.movieapp.ui.details.DetailsActivity
import retrofit2.Call

class ListActivity : AppCompatActivity(), IClickListener {
    private lateinit var activityListBinding: ActivityListBinding

    private lateinit var movieListRepository: MovieListRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ListActivityViewModel
    private lateinit var title: String
    private lateinit var moviesPage: (Int) -> Call<MovieResponse>
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        setToolbarTitle()

        sharedPref = getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        movieListRepository = MovieListRepository(moviesPage)
        viewModelFactory = ViewModelFactory(movieListRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListActivityViewModel::class.java)

        viewModel.title = title
        activityListBinding.apply {
            this.lifecycleOwner = this@ListActivity
            this.recyclerView.viewModel = this@ListActivity.viewModel
            this.recyclerView.iClickListener = this@ListActivity
        }

        // Picasso.get().isLoggingEnabled = true
        // Picasso.get().setIndicatorsEnabled(true)
        activityListBinding.recyclerView.swipeToRefresh.setOnRefreshListener {
            viewModel.invalidateList()
            activityListBinding.recyclerView.swipeToRefresh.isRefreshing = false
        }

        activityListBinding.recyclerView.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        MovieDetailsRepository.load = true
        sharedPref[CURRENT_SECOND] = 0F
    }

    override fun onPause() {
        HelperFunctions.unregisterInternetReceiver(this)
        super.onPause()
    }

    override fun onMovieClick(id: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, id)
        startActivity(intent)
    }

    override fun onRetryClick(type: Int) {
        if (type == 0) {
            viewModel.invalidateList()
        }
        else {
            viewModel.invalidateList()
            println("pol")
            val p = viewModel.moviePagedList.value?.lastKey
            println(p)
        }
    }

    private fun setToolbarTitle() {
        when (intent.getStringExtra(TYPE_OF_MOVIE)) {
            null, POPULAR -> { title = getString(R.string.popular); moviesPage = RetrofitCallback::tMDbPopularMoviesPage }
            NOW_PLAYING -> { title = getString(R.string.now_playing); moviesPage = RetrofitCallback::tMDbNowPlayingMoviesPage }
            UPCOMING -> { title = getString(R.string.upcoming); moviesPage = RetrofitCallback::tMDbUpcomingMoviesPage }
            TOP_RATED -> { title = getString(R.string.top_rated); moviesPage = RetrofitCallback::tMDbTopRatedMoviesPage }
        }
    }
}