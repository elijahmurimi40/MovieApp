package com.fortie40.movieapp

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fortie40.movieapp.workmanagers.NotifyUserForUpdate

class CheckForUpdateLifeCycleObserver(val context: Context, lifecycle: Lifecycle) : LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startWorkManager() {
        NotifyUserForUpdate.NotifyUser.notifyUser(context)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopWorkManager() {
        NotifyUserForUpdate.NotifyUser.stopWorkManager(context)
    }
}