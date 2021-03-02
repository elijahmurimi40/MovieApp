package com.fortie40.movieapp.ui.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fortie40.movieapp.*
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.data.repository.MovieListDataSource
import com.fortie40.movieapp.data.repository.MovieListRepository
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.databinding.ActivityListBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.helperclasses.MovieListStaggeredGridLayoutManager
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.helperclasses.PreferenceHelper.set
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.interfaces.INetworkStateReceiver
import com.fortie40.movieapp.ui.details.DetailsActivity
import retrofit2.Call

class ListActivity : AppCompatActivity(), IClickListener, INetworkStateReceiver{
    private lateinit var activityListBinding: ActivityListBinding

    private lateinit var movieListRepository: MovieListRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ListActivityViewModel
    private lateinit var title: String
    private lateinit var moviesPage: (Int) -> Call<MovieResponse>
    private lateinit var sharedPref: SharedPreferences

    private var isOnCreateCalled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        setToolbarTitle()

        savedInstanceState?.run {
            MovieListDataSource.lastPage = getInt(LAST_PAGE, 0)
        }

        sharedPref = getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        movieListRepository = MovieListRepository(moviesPage, this)
        viewModelFactory = ViewModelFactory(movieListRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LIST_ACTIVITY_KEY, ListActivityViewModel::class.java)

        viewModel.title = title
        activityListBinding.apply {
            this.lifecycleOwner = this@ListActivity
            this.recyclerView.viewModel = this@ListActivity.viewModel
            this.recyclerView.iClickListener = this@ListActivity
        }

        // Picasso.get().isLoggingEnabled = true
        // Picasso.get().setIndicatorsEnabled(true)
        activityListBinding.recyclerView.swipeToRefresh.setOnRefreshListener {
            val adapter = activityListBinding.recyclerView.rvMovieList.adapter as ListActivityAdapter
            adapter.setNetWorkState(NetworkState.LOADED)
            viewModel.invalidateList()
            activityListBinding.recyclerView.swipeToRefresh.isRefreshing = false
        }

        activityListBinding.recyclerView.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        isOnCreateCalled = true
    }

    override fun onStart() {
        super.onStart()
        if (!isOnCreateCalled) recreate()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        activityListBinding.recyclerView.progressBarRestore.visibility = View.VISIBLE
        savedInstanceState.run {
            val offSet = getParcelable<Parcelable>("P")

            Handler().postDelayed({
                (activityListBinding.recyclerView.rvMovieList.layoutManager as MovieListStaggeredGridLayoutManager)
                    .onRestoreInstanceState(offSet)
                activityListBinding.recyclerView.progressBarRestore.visibility = View.GONE
            }, 300)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val rv = activityListBinding.recyclerView.rvMovieList
        val layoutManager =
            rv.layoutManager as MovieListStaggeredGridLayoutManager
        val p = layoutManager.onSaveInstanceState()
        outState.run {
            putInt(LAST_PAGE, MovieListDataSource.lastPage)
            putParcelable("P", p)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        MovieDetailsRepository.load = true
        sharedPref[CURRENT_SECOND] = 0F

        HelperFunctions.registerInternetReceiver(this)
        if (!NetworkStateReceiver.isNetworkAvailable(this))
            networkNotAvailable()
    }

    override fun onPause() {
        HelperFunctions.unregisterInternetReceiver(this)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) {
            isOnCreateCalled = false
            HelperFunctions.clearViewModel(this, LIST_ACTIVITY_KEY)
        }
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
            viewModel.retry()
        }
    }

    override fun networkAvailable() {
        val textView = activityListBinding.recyclerView.networkReceiver.status
        val appBarLayout = activityListBinding.recyclerView.appBarLayout

        HelperFunctions.networkAvailable(textView, appBarLayout)

        if (activityListBinding.recyclerView.textErrorPopular.visibility == View.VISIBLE)
            viewModel.invalidateList()
    }

    override fun networkNotAvailable() {
        val textView = activityListBinding.recyclerView.networkReceiver.status
        val appBarLayout = activityListBinding.recyclerView.appBarLayout

        HelperFunctions.networkNotAvailable(textView, appBarLayout)
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