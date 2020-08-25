package com.fortie40.movieapp.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.MOVIE_ID
import com.fortie40.movieapp.R
import com.fortie40.movieapp.databinding.ActivityDetailsBinding
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var activityDetailsBinding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        activityDetailsBinding.toolbar.setOnClickListener {
            onBackPressed()
        }

        val movieId = intent.getIntExtra(MOVIE_ID, 1)

        id.text = movieId.toString()
    }
}