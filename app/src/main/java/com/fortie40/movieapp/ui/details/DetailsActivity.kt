package com.fortie40.movieapp.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.MOVIE_ID
import com.fortie40.movieapp.R
import com.fortie40.movieapp.data.repository.MovieDetailsRepository
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.databinding.ActivityDetailsBinding
import com.fortie40.movieapp.helperclasses.ViewModelFactory

class DetailsActivity : AppCompatActivity() {
    private lateinit var activityDetailsBinding: ActivityDetailsBinding

    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        activityDetailsBinding.toolbar.setOnClickListener {
            onBackPressed()
        }

        val movieId = intent.getIntExtra(MOVIE_ID, 605116)

        movieDetailsRepository = MovieDetailsRepository(RetrofitCallback::tMDbMovieDetailsId)
        viewModelFactory = ViewModelFactory(movieDetailsRepository, movieId)
        val viewModel: DetailsActivityViewModel by viewModels {viewModelFactory}

        activityDetailsBinding.apply {
            this.lifecycleOwner = this@DetailsActivity
            this.viewModel = viewModel
        }
    }
}