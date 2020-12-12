package com.fortie40.movieapp.helperclasses

import android.content.Context
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.fortie40.movieapp.NOTIFY_USER_FOR_UPDATE_TIME
import com.fortie40.movieapp.R
import com.fortie40.movieapp.SHARED_PREF_FILE
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.fortie40.movieapp.workmanagers.NotifyUserForUpdate
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.fortie40.movieapp.helperclasses.PreferenceHelper.set
import java.util.concurrent.TimeUnit

object HelperFunctions {
    fun viewInflater(parent: ViewGroup, layout: Int): ViewDataBinding {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate(layoutInflater, layout, parent, false)
    }

    fun clearViewModel(owner: ViewModelStoreOwner, key: String) {
        ViewModelProvider(owner).get(key, EmptyViewModel::class.java)
    }

    private var networkStateReceiver: NetworkStateReceiver? = null
    fun registerInternetReceiver(context: Context) {
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        networkStateReceiver = NetworkStateReceiver()
        context.registerReceiver(networkStateReceiver, intentFilter)
    }

    fun unregisterInternetReceiver(context: Context) {
        if (networkStateReceiver != null) {
            context.unregisterReceiver(networkStateReceiver)
            networkStateReceiver = null
        }
    }

    fun networkAvailable(tv: TextView, abl: AppBarLayout) {
        tv.text = tv.context.getString(R.string.back_online)
        tv.setBackgroundColor(tv.context.resources.getColor(android.R.color.holo_blue_dark))

        if (tv.visibility == View.VISIBLE) {
            CoroutineScope(IO).launch {
                delay(2000)
                withContext(Main) {
                    tv.visibility = View.INVISIBLE
                    val lm = abl.layoutParams as ViewGroup.MarginLayoutParams
                    lm.topMargin = 0
                    abl.layoutParams = lm
                }
            }
        }
    }

    fun networkNotAvailable(tv: TextView, abl: AppBarLayout) {
        tv.visibility = View.VISIBLE
        tv.text = tv.context.getString(R.string.no_internet)
        tv.setBackgroundColor(tv.context.resources.getColor(android.R.color.black))
        val height = tv.height

        val lm = abl.layoutParams as ViewGroup.MarginLayoutParams
        lm.topMargin = height
        abl.layoutParams = lm
    }

    fun showIndefiniteSnackBar(view: View) {
        val sharedPref =
            view.context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        val snackBar = Snackbar.make(view, R.string.new_app_version, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.dismiss) {
            snackBar.dismiss()
        }
        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                val actionEvent = event == DISMISS_EVENT_ACTION
                val swipeEvent = event == DISMISS_EVENT_SWIPE

                if (actionEvent || swipeEvent) {
                    val dateToNotify = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)
                    sharedPref[NOTIFY_USER_FOR_UPDATE_TIME] = dateToNotify
                    NotifyUserForUpdate.NotifyUser.isSnackBarVisible = false
                }
            }
        })
        snackBar.show()
    }
}