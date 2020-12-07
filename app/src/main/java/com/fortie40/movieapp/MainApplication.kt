package com.fortie40.movieapp

import androidx.multidex.MultiDexApplication
import timber.log.Timber

//class MainApplication : Application() {
class MainApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}