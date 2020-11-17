package com.fortie40.movieapp.ui.trailer

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.view.OrientationEventListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.*
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.fortie40.movieapp.databinding.ActivityTrailerBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.helperclasses.PreferenceHelper.get
import com.fortie40.movieapp.helperclasses.PreferenceHelper.set
import com.fortie40.movieapp.interfaces.INetworkStateReceiver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController

class TrailerActivity : AppCompatActivity(), INetworkStateReceiver {
    private lateinit var activityTrailerBinding: ActivityTrailerBinding
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var orientationEventListener: OrientationEventListener
    private lateinit var sharedPref: SharedPreferences
    private lateinit var tracker: YouTubePlayerTracker
    private lateinit var youTubePlayerListener: AbstractYouTubePlayerListener
    private lateinit var playerUiController: PlayerUiController

    private var currentOrientation = 0
    private var movieKey: String? = null
    private var isBackPressed = false
    private var movieTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        activityTrailerBinding = DataBindingUtil.setContentView(this, R.layout.activity_trailer)

        sharedPref = getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        movieKey = intent.getStringExtra(MOVIE_KEY)
        if (movieKey == null)
            movieKey = "LX6kVRsdXW4"

        movieTitle = intent.getStringExtra(MOVIE_TITLE)
        if (movieTitle == null)
            movieTitle = ""

        sharedPref[MOVIE_KEY] = movieKey!!

        tracker = YouTubePlayerTracker()
        youTubePlayerView = activityTrailerBinding.trailer
        playerUiController = youTubePlayerView.getPlayerUiController()
        activityTrailerBinding.backButton.setOnClickListener {
            onBackPressed()
        }

        enterExitFullScreen()
        lifecycle.addObserver(youTubePlayerView)
        initYoutubePlayerView()

        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (autoRotateIsOn()) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                    enterExitFullScreen()
                }
            }
        }
        orientationEventListener.enable()

        val movieKeyS = sharedPref[MOVIE_KEY, ""]
        val startSeconds = sharedPref[CURRENT_SECOND, 0F]
        youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.addListener(tracker)
                // youTubePlayer.loadVideo(movieKeyS!!, startSeconds!!)
                youTubePlayer.loadOrCueVideo(lifecycle, movieKeyS!!, startSeconds!!)
                sharedPref[CURRENT_SECOND] = 0F
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState,
            ) {
                super.onStateChange(youTubePlayer, state)
                if (state == PlayerConstants.PlayerState.BUFFERING || state == PlayerConstants.PlayerState.UNSTARTED) {
                    activityTrailerBinding.progressBar3.visibility = View.VISIBLE
                    playerUiController.showBufferingProgress(false)
                } else {
                    activityTrailerBinding.progressBar3.visibility = View.GONE
                    playerUiController.showBufferingProgress(true)
                }
            }
        }

        youTubePlayerView.addYouTubePlayerListener(youTubePlayerListener)
    }

    override fun onResume() {
        super.onResume()
        isBackPressed = false
        HelperFunctions.registerInternetReceiver(this)
        if (!NetworkStateReceiver.isNetworkAvailable(this))
            networkNotAvailable()

        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.play()
            }
        })
    }

    override fun onPause() {
        val currentSecond = tracker.currentSecond
        if (!isBackPressed)
            sharedPref[CURRENT_SECOND] = currentSecond
        HelperFunctions.unregisterInternetReceiver(this)
        super.onPause()
    }

    override fun onBackPressed() {
        isBackPressed = true
        super.onBackPressed()
    }

    override fun onDestroy() {
        orientationEventListener.disable()
        super.onDestroy()
    }

    override fun networkAvailable() {
        val textView = activityTrailerBinding.networkReceiver.status
        val actionBar = activityTrailerBinding.barLayout

        HelperFunctions.networkAvailable(textView, actionBar)
    }

    override fun networkNotAvailable() {
        val textView = activityTrailerBinding.networkReceiver.status
        val actionBar = activityTrailerBinding.barLayout

        HelperFunctions.networkNotAvailable(textView, actionBar)
    }

    private fun enterExitFullScreen() {
        // currentOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        currentOrientation = if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            youTubePlayerView.enterFullScreen()
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            youTubePlayerView.exitFullScreen()
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun initYoutubePlayerView() {
        playerUiController.setVideoTitle(movieTitle!!)
        playerUiController.setFullScreenButtonClickListener {
            if (autoRotateIsOn())
                return@setFullScreenButtonClickListener
            requestedOrientation = if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                youTubePlayerView.exitFullScreen()
                currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                youTubePlayerView.enterFullScreen()
                currentOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }
    }

    private fun autoRotateIsOn(): Boolean {
        return Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
    }
}