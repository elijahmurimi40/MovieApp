package com.fortie40.movieapp.ui.trailer

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.OrientationEventListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.fortie40.movieapp.*
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.fortie40.movieapp.databinding.ActivityTrailerBinding
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.interfaces.INetworkStateReceiver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.fortie40.movieapp.helperclasses.PreferenceHelper.set
import com.fortie40.movieapp.helperclasses.PreferenceHelper.get
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker

class TrailerActivity : AppCompatActivity(), INetworkStateReceiver {
    private lateinit var activityTrailerBinding: ActivityTrailerBinding
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var orientationEventListener: OrientationEventListener
    private lateinit var sharedPref: SharedPreferences
    private lateinit var tracker: YouTubePlayerTracker

    private var savedOrientation: Int? = null
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
        activityTrailerBinding.backButton.setOnClickListener {
            onBackPressed()
        }

        enterExitFullScreen()
        lifecycle.addObserver(youTubePlayerView)
        initYoutubePlayerView()

        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                val isPortrait = isPortrait(orientation)
                if (savedOrientation == null)
                    return

                if (!isPortrait && savedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    savedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                } else if (isPortrait && savedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    savedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                }
            }
        }
        orientationEventListener.enable()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        enterExitFullScreen()
    }

    override fun onResume() {
        super.onResume()
        isBackPressed = false
        HelperFunctions.registerInternetReceiver(this)
        if (!NetworkStateReceiver.isNetworkAvailable(this))
            networkNotAvailable()

        val movieKeyS = sharedPref[MOVIE_KEY, ""]
        val startSeconds = sharedPref[CURRENT_SECOND, 0F]
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.addListener(tracker)
                youTubePlayer.loadVideo(movieKeyS!!, startSeconds!!)
                sharedPref[CURRENT_SECOND] = 0F
            }
        })
    }

    override fun onPause() {
        val currentSecond = tracker.currentSecond
        if (!isBackPressed)
            sharedPref[CURRENT_SECOND] = currentSecond
        youTubePlayerView.release()
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
        currentOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen()
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            youTubePlayerView.exitFullScreen()
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        savedOrientation = currentOrientation
    }

    private fun initYoutubePlayerView() {
        val playerUiController = youTubePlayerView.getPlayerUiController()
        playerUiController.setVideoTitle(movieTitle!!)
        playerUiController.setFullScreenButtonClickListener {
            requestedOrientation = if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun isPortrait(orientation: Int): Boolean {
        if (orientation < 45 || orientation > 315)
            return true

        return false
    }
}