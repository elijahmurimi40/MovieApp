package com.fortie40.movieapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import com.fortie40.movieapp.helperclasses.HelperFunctions
import com.fortie40.movieapp.workmanagers.NotifyUserForUpdate
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.swipe_to_refresh
import kotlinx.android.synthetic.main.activity_trailer.*
import kotlinx.android.synthetic.main.recycler_view.*

class LifecycleHandler : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
        when (activity::class.simpleName) {
            "MainActivity" -> {
                val view = activity.swipe_to_refresh
                setNotifyUserView(view)
            }
            "DetailsActivity" -> {
                val view = activity.coordinatorLayout
                setNotifyUserView(view)
            }
            "ListActivity" -> {
                val view = activity.rv_movie_list
                setNotifyUserView(view)
            }
            "TrailerActivity" -> {
                val view = activity.trailer
                setNotifyUserView(view)
            }
            else -> NotifyUserForUpdate.NotifyUser.view = null
        }
    }

    override fun onActivityPaused(activity: Activity) {
        NotifyUserForUpdate.NotifyUser.view = null
    }

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    private fun setNotifyUserView(view: View) {
        if (NotifyUserForUpdate.NotifyUser.isSnackBarVisible)
            HelperFunctions.showIndefiniteSnackBar(view)
        NotifyUserForUpdate.NotifyUser.view = view
    }
}