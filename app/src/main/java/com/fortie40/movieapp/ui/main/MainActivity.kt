package com.fortie40.movieapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fortie40.movieapp.*
import com.fortie40.movieapp.data.models.Movie
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.databinding.ActivityMainBinding
import com.fortie40.movieapp.ui.list.ListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        recycler_view_main.setHasFixedSize(true)
        recycler_view_main.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = MainAdapter()
        recycler_view_main.adapter = adapter
        data()
        viewAllClick()
    }

    private fun data() {
        val list = arrayListOf<Movie>()
        val p = arrayListOf<MovieResponse>()
        for (i in 1..20) {
            val p2 = Movie(i, "title$i", "", "", "", i)
            list.add(p2)
        }
        for (i in 1..5) {
            val m = MovieResponse("Title$i", i, i, i, i, list)
            p.add(m)
        }

        adapter.submitList(p)
    }

    private fun viewAllClick() {
//        activityMain2Binding.popular.setOnClickListener { startListActivity(POPULAR) }
//        activityMain2Binding.nowPlaying.setOnClickListener { startListActivity(NOW_PLAYING) }
//        activityMain2Binding.upcoming.setOnClickListener { startListActivity(UPCOMING) }
//        activityMain2Binding.topRated.setOnClickListener { startListActivity(TOP_RATED) }
    }

    private fun startListActivity(value: String) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra(TYPE_OF_MOVIE, value)
        startActivity(intent)
    }
}