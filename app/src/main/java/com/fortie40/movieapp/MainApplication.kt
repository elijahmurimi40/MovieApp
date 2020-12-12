package com.fortie40.movieapp

import androidx.multidex.MultiDexApplication
import com.fortie40.movieapp.workmanagers.CheckLatestAppVersion
import com.fortie40.movieapp.workmanagers.NotifyUserForUpdate
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

        registerActivityLifecycleCallbacks(LifecycleHandler())
        CheckLatestAppVersion.CheckForLatest.checkForLatestUpdate(applicationContext)
        NotifyUserForUpdate.NotifyUser.notifyUser(applicationContext)
    }

    /**
    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
    super.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
    super.unregisterActivityLifecycleCallbacks(callback)
    }
     */
}