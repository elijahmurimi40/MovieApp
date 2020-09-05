package com.fortie40.movieapp.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fortie40.movieapp.*
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MovieListRepository
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.databinding.ActivityListBinding
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.ui.details.DetailsActivity
import retrofit2.Call

class ListActivity : AppCompatActivity(), IClickListener {
    private lateinit var activityListBinding: ActivityListBinding

    private lateinit var movieListRepository: MovieListRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ListActivityViewModel
    private lateinit var title: String
    private lateinit var moviesPage: (Int) -> Call<MovieResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        setToolbarTitle()

        movieListRepository = MovieListRepository(moviesPage)
        viewModelFactory = ViewModelFactory(movieListRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListActivityViewModel::class.java)

        viewModel.title = title
        activityListBinding.apply {
            this.lifecycleOwner = this@ListActivity
            this.recyclerView.viewModel = this@ListActivity.viewModel
        }

        // Picasso.get().isLoggingEnabled = true
        // Picasso.get().setIndicatorsEnabled(true)
        activityListBinding.recyclerView.swipeToRefresh.setOnRefreshListener {
            viewModel.moviePagedList.value?.dataSource?.invalidate()
            activityListBinding.recyclerView.swipeToRefresh.isRefreshing = false
        }
    }

    override fun onMovieClick(id: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, id)
        startActivity(intent)
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