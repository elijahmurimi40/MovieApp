package com.fortie40.movieapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.*
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.fortie40.movieapp.data.models.MovieResponse
import com.fortie40.movieapp.data.repository.MainRepository
import com.fortie40.movieapp.data.retrofitservices.RetrofitCallback
import com.fortie40.movieapp.databinding.ActivityMainBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.helperclasses.MovieLinearLayoutManager
import com.fortie40.movieapp.helperclasses.NetworkState
import com.fortie40.movieapp.helperclasses.ViewModelFactory
import com.fortie40.movieapp.interfaces.IClickListener
import com.fortie40.movieapp.interfaces.INetworkStateReceiver
import com.fortie40.movieapp.ui.details.DetailsActivity
import com.fortie40.movieapp.ui.list.ListActivity
import retrofit2.Call

class MainActivity : AppCompatActivity(), IClickListener, INetworkStateReceiver {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    private lateinit var mainRepository: MainRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var callMovieResponse: Call<MovieResponse>

    private lateinit var recyclerViewMain: RecyclerView

    private var id: Int = 1
    private val response: MutableList<MovieResponse?> = arrayListOf()
    private var moviesHasObservers = false
    private var isRequesting = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        savedInstanceState?.run {
            id = getInt(ID_TITLE, 0) + 1
            val mr = getParcelableArrayList<MovieResponse>(RESPONSE_ARRAY) as List<MovieResponse>
            response.clear()
            response.addAll(mr)
        }
        setCallMovieResponse(id)

        mainRepository = MainRepository()
        viewModelFactory = ViewModelFactory(mainRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MAIN_ACTIVITY_KEY, MainViewModel::class.java)
        viewModel.id = id

        activityMainBinding.apply {
            this.lifecycleOwner = this@MainActivity
            this.viewModel = this@MainActivity.viewModel
        }
        adapter = MainAdapter(this)
        recyclerViewMain = activityMainBinding.recyclerViewMain
        recyclerViewMain.adapter = adapter

        val moviesObserver = {
            moviesHasObservers = true
            isRequesting = true
            if (id > 1) {
                adapter.submitList(response.toMutableList())
            }
            viewModel.movies(callMovieResponse).observe(this, {
                it.map { mr ->
                    setTitle(mr)
                }

                if (it.isNotEmpty()) {
                    response.addAll(it)
                    id += 1
                }
                adapter.submitList(response.toMutableList())
            })
        }

        if (id > NUMBER_OF_REQUEST) {
            adapter.submitList(response.toMutableList())
            isRequesting = false
        } else {
            moviesObserver()
        }

        viewModel.networkState.observe(this, {
            if (it == NetworkState.LOADED) {
                if (id <= NUMBER_OF_REQUEST) {
                    setCallMovieResponse(id)
                    viewModel.movies(callMovieResponse)
                } else {
                    isRequesting = false
                    activityMainBinding.swipeToRefresh.isRefreshing = false
                }
            } else if (it == NetworkState.ERROR) {
                isRequesting = false
                activityMainBinding.swipeToRefresh.isRefreshing = false
            }

            if (!viewModel.listIsEmpty() || id > 1)
                adapter.setNetWorkState(it)
        })

        activityMainBinding.swipeToRefresh.setOnRefreshListener {
            if (isRequesting) return@setOnRefreshListener
            isRequesting = true
            response.clear()
            adapter.resetScrollState()
            id = 1
            viewModel.id = id
            setCallMovieResponse(id)
            if (moviesHasObservers) {
                adapter.submitList(response.toMutableList())
                viewModel.movies(callMovieResponse)
                activityMainBinding.swipeToRefresh.isRefreshing = false
            } else {
                adapter.submitList(response.toMutableList())
                moviesObserver()
                activityMainBinding.swipeToRefresh.isRefreshing = false
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            val positionIndex = getInt(POSITION_INDEX, -1)
            val offset = getInt(OFFSET)
            if (positionIndex != -1) {
                (recyclerViewMain.layoutManager as MovieLinearLayoutManager)
                    .scrollToPositionWithOffset(positionIndex, offset)
            }
        }
        adapter.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (response.isNotEmpty()) {
            val lastMovieResponse = adapter.currentList
            val lastId = lastMovieResponse[lastMovieResponse.size - 1].id

            val layoutManager =
                (recyclerViewMain.layoutManager as MovieLinearLayoutManager)
            val startView = recyclerViewMain.getChildAt(0)
            val positionIndex = layoutManager.findFirstVisibleItemPosition()
            val offSet = if (startView == null) 0 else startView.top - recyclerViewMain.top

            outState.run {
                putInt(ID_TITLE, lastId)
                putParcelableArrayList(RESPONSE_ARRAY, ArrayList<MovieResponse>(lastMovieResponse))
                putInt(POSITION_INDEX, positionIndex)
                putInt(OFFSET, offSet)
            }
            HelperFunctions.clearViewModel(this, MAIN_ACTIVITY_KEY)
            adapter.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        HelperFunctions.registerInternetReceiver(this)
        if (!NetworkStateReceiver.isNetworkAvailable(this))
            networkNotAvailable()
    }

    override fun onPause() {
        HelperFunctions.unregisterInternetReceiver(this)
        super.onPause()
    }

    override fun onMovieClick(id: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, id)
        startActivity(intent)
    }

    override fun onMoreClick(title: String) {
        when(title) {
            getString(R.string.popular) -> startListActivity(POPULAR)
            getString(R.string.now_playing) -> startListActivity(NOW_PLAYING)
            getString(R.string.upcoming) -> startListActivity(UPCOMING)
            getString(R.string.top_rated) -> startListActivity(TOP_RATED)
        }
    }

    override fun networkAvailable() {
        val textView = activityMainBinding.networkReceiver.status
        val actionBar = activityMainBinding.actionBar

        HelperFunctions.networkAvailable(textView, actionBar)
    }

    override fun networkNotAvailable() {
        val textView = activityMainBinding.networkReceiver.status
        val actionBar = activityMainBinding.actionBar

        HelperFunctions.networkNotAvailable(textView, actionBar)
    }

    private fun startListActivity(value: String) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra(TYPE_OF_MOVIE, value)
        startActivity(intent)
    }

    private fun setCallMovieResponse(id: Int) {
        when(id) {
            1 -> callMovieResponse = RetrofitCallback.tMDbPopularMoviesPage(1)
            2 -> callMovieResponse = RetrofitCallback.tMDbNowPlayingMoviesPage(1)
            3 -> callMovieResponse = RetrofitCallback.tMDbUpcomingMoviesPage(1)
            4 -> callMovieResponse = RetrofitCallback.tMDbTopRatedMoviesPage(1)
        }
    }

    private fun setTitle(mr: MovieResponse?) {
        when(id) {
            1 -> {mr?.title = getString(R.string.popular); mr?.id = id}
            2 -> {mr?.title = getString(R.string.now_playing); mr?.id = id}
            3 -> {mr?.title = getString(R.string.upcoming); mr?.id = id}
            4 -> {mr?.title = getString(R.string.top_rated); mr?.id = id}
        }
    }
}