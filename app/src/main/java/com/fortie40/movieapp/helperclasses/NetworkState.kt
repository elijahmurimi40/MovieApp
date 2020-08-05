package com.fortie40.movieapp.helperclasses

import timber.log.Timber

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(status: Status, msg: String) {
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Something Went Wrong")
        val END_OF_LIST: NetworkState = NetworkState(Status.FAILED, "You have reached the end")
    }

    init {
        Timber.d("$status $msg")
    }
}