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
import com.fortie40.movieapp.R
import com.fortie40.movieapp.broadcastreceivers.NetworkStateReceiver
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
}