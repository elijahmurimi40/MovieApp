package com.fortie40.movieapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.*
import com.fortie40.movieapp.databinding.ActivityMainBinding
import com.fortie40.movieapp.ui.list.ListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewAllClick()
    }

    private fun viewAllClick() {
        activityMainBinding.popular.setOnClickListener { startListActivity(POPULAR) }
        activityMainBinding.nowPlaying.setOnClickListener { startListActivity(NOW_PLAYING) }
        activityMainBinding.upcoming.setOnClickListener { startListActivity(UPCOMING) }
        activityMainBinding.topRated.setOnClickListener { startListActivity(TOP_RATED) }
    }

    private fun startListActivity(value: String) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra(TYPE_OF_MOVIE, value)
        startActivity(intent)
    }
}