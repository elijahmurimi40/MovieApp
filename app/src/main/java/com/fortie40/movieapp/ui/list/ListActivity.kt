package com.fortie40.movieapp.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fortie40.movieapp.MOVIE_ID
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.repository.MovieListRepository
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.databinding.ActivityListBinding
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.ui.details.DetailsActivity

class ListActivity : AppCompatActivity(), IClickListener {
    private lateinit var activityListBinding: ActivityListBinding

    private lateinit var movieListRepository: MovieListRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        movieListRepository = MovieListRepository(RetrofitCallback::tMDbPopularMoviesPage)
        viewModelFactory = ViewModelFactory(movieListRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListActivityViewModel::class.java)

        viewModel.title = "Popular"
        activityListBinding.apply {
            this.lifecycleOwner = this@ListActivity
            this.recyclerView.viewModel = this@ListActivity.viewModel
        }

        // Picasso.get().isLoggingEnabled = true
        // Picasso.get().setIndicatorsEnabled(true)
    }

    override fun onMovieClick(id: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, id)
        startActivity(intent)
    }
}