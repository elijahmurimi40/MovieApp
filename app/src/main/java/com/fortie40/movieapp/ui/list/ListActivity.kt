package com.fortie40.movieapp.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.MovieRepository
import com.fortie40.movieapp.data.TMDbMovieViewModelFactory
import com.fortie40.movieapp.databinding.ActivityListBinding
import com.fortie40.movieapp.retrofitservices.RetrofitCallback

class ListActivity : AppCompatActivity() {
    private lateinit var activityListBinding: ActivityListBinding

    private lateinit var repository: MovieRepository
    private lateinit var viewModelFactory: TMDbMovieViewModelFactory
    private lateinit var viewModel: ListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        repository = MovieRepository(RetrofitCallback::movie)
        viewModelFactory = TMDbMovieViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListActivityViewModel::class.java)

        viewModel.title = "Popular"
        activityListBinding.apply {
            this.lifecycleOwner = this@ListActivity
            this.recyclerView.viewModel = this@ListActivity.viewModel
        }

        // Picasso.get().isLoggingEnabled = true
        // Picasso.get().setIndicatorsEnabled(true)
    }
}