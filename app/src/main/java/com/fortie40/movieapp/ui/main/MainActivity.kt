package com.fortie40.movieapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.ID_TITLE
import com.fortie40.movieapp.R
import com.fortie40.movieapp.TYPE_OF_MOVIE
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MainRepository
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.databinding.ActivityMainBinding
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.ui.list.ListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    private lateinit var mainRepository: MainRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    private var id: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState != null)
            id = savedInstanceState.getInt(ID_TITLE, 1)

        mainRepository = MainRepository(RetrofitCallback::tMDbPopularMoviesPage)
        viewModelFactory = ViewModelFactory(mainRepository)
        val viewModel: MainViewModel by viewModels { viewModelFactory }
        this.viewModel = viewModel

        activityMainBinding.apply {
            this.lifecycleOwner = this@MainActivity
            this.viewModel = viewModel
        }

        adapter = MainAdapter()
        recycler_view_main.adapter = adapter

        val response: MutableList<MovieResponse?> = arrayListOf()
        viewModel.movies.observe(this, {
            it.map { mr ->
                when(id) {
                    1 -> {mr?.title = getString(R.string.popular); mr?.id = id}
                    2 -> {mr?.title = getString(R.string.now_playing); mr?.id = id}
                    3 -> {mr?.title = getString(R.string.upcoming); mr?.id = id}
                    4 -> {mr?.title = getString(R.string.top_rated); mr?.id = id}
                }
            }
            response.addAll(it)
            adapter.submitList(response.toMutableList())
        })

        viewModel.networkState.observe(this, {
            if (it == NetworkState.LOADED) {
                movieListPage()
            }
        })

        viewAllClick()
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

    private fun movieListPage() {
        id += 1
        if (id <= 4) {
            when(id) {
                2 -> viewModel.getMoviesNext(RetrofitCallback.tMDbNowPlayingMoviesPage(1))
                3 -> viewModel.getMoviesNext(RetrofitCallback.tMDbUpcomingMoviesPage(1))
                4 -> viewModel.getMoviesNext(RetrofitCallback.tMDbTopRatedMoviesPage(1))
            }
        }
    }
}