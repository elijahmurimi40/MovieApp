package com.fortie40.movieapp.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.fortie40.movieapp.interfaces.INetworkStateReceiver

class NetworkStateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNetworkAvailable(context!!)) {
            (context as INetworkStateReceiver).networkAvailable()
        } else {
            (context as INetworkStateReceiver).networkNotAvailable()
        }
    }

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = manager.activeNetwork ?: return false
                val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false
                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                val networkInfo = manager.activeNetworkInfo ?: return false
                return networkInfo.isConnected
            }
        }
    }
}