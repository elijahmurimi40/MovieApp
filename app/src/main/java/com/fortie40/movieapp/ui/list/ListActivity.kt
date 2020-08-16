package com.fortie40.movieapp.ui.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.R
import com.fortie40.movieapp.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var activityListBinding: ActivityListBinding

    private val viewModel by viewModels<ListActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        viewModel.title = "Popular"
        activityListBinding.apply {
            this.lifecycleOwner = this@ListActivity
            this.recyclerView.viewModel = this@ListActivity.viewModel
        }

        // Picasso.get().isLoggingEnabled = true
        // Picasso.get().setIndicatorsEnabled(true)
    }
}