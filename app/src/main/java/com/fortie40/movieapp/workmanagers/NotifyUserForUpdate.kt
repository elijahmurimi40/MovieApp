package com.fortie40.movieapp.workmanagers

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.*
import com.fortie40.movieapp.BuildConfig
import com.fortie40.movieapp.NOTIFY_USER_FOR_UPDATE
import com.fortie40.movieapp.NOTIFY_USER_FOR_UPDATE_TIME
import com.fortie40.movieapp.SHARED_PREF_FILE
import com.fortie40.movieapp.data.models.Response
import com.fortie40.movieapp.data.roomdatabase.MovieAppRoomDatabase
import com.fortie40.movieapp.helperclasses.HelperFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import com.fortie40.movieapp.helperclasses.PreferenceHelper.get

class NotifyUserForUpdate(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val database = MovieAppRoomDatabase(context)
    private val dao = database.checkLatestDao()
    private val sharedPref = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)

    override fun doWork(): Result {
        var isVisible: Boolean
        val isViewNull = NotifyUser.view != null
        val dateToNotify = sharedPref[NOTIFY_USER_FOR_UPDATE_TIME, 0L]
        val currentTime = System.currentTimeMillis()
        val notifyUser = currentTime >= dateToNotify!!
        CoroutineScope(IO).launch {
            withContext(Main) {
                isVisible =
                    ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
            }

            val response: Response? = dao.getLatestVersion()
            if (response != null && isVisible && isViewNull && notifyUser) {
                val versionCode = response.version_code
                val gradleVersionCode = BuildConfig.VERSION_CODE
                if (versionCode > gradleVersionCode) {
                    withContext(Main) {
                        if (!NotifyUser.isSnackBarVisible)
                            HelperFunctions.showIndefiniteSnackBar(NotifyUser.view!!)

                        NotifyUser.isSnackBarVisible = true
                    }
                }
            }
        }

        return Result.success()
    }

    object NotifyUser {
        var view: View? = null
        var isSnackBarVisible = false

        fun notifyUser(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val notifyUserForUpdate =
                PeriodicWorkRequestBuilder<NotifyUserForUpdate>(15, TimeUnit.MINUTES)
                    .build()

            workManager.enqueueUniquePeriodicWork(NOTIFY_USER_FOR_UPDATE,
                ExistingPeriodicWorkPolicy.REPLACE,
                notifyUserForUpdate)
        }

        fun stopWorkManager(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.cancelUniqueWork(NOTIFY_USER_FOR_UPDATE)
        }
    }
}