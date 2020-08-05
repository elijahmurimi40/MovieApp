package com.fortie40.movieapp.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.R
import com.fortie40.movieapp.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var activityDetailsBinding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
    }
}