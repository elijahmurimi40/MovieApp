package com.fortie40.movieapp.interfaces

interface INetworkStateReceiver {
    fun networkAvailable()

    fun networkNotAvailable()
}