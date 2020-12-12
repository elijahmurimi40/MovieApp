package com.fortie40.movieapp.workmanagers

import android.content.Context
import androidx.work.*
import com.fortie40.movieapp.BuildConfig
import com.fortie40.movieapp.CHECK_FOR_LATEST
import com.fortie40.movieapp.LATEST_APP_VERSION
import com.fortie40.movieapp.data.models.CheckLatestVersion
import com.fortie40.movieapp.data.retrofitservices.ICheckLatestVersion
import com.fortie40.movieapp.data.retrofitservices.RetrofitBuilder
import com.fortie40.movieapp.data.roomdatabase.MovieAppRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class CheckLatestAppVersion(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val latestVersion =
            RetrofitBuilder.buildService(ICheckLatestVersion::class.java, LATEST_APP_VERSION)
        val call = latestVersion.getLatestVersion()
        call.enqueue(object : Callback<CheckLatestVersion> {
            override fun onFailure(call: Call<CheckLatestVersion>, t: Throwable) = Unit
            override fun onResponse(
                call: Call<CheckLatestVersion>, response: Response<CheckLatestVersion>,
            ) {
                val checkLatestVersion = response.body()
                val versionCode = checkLatestVersion?.success?.version_code
                val gradleVersionCode = BuildConfig.VERSION_CODE
                if (versionCode != null && versionCode > gradleVersionCode) {
                    checkLatestVersion.success.id = 1
                    val database = MovieAppRoomDatabase(context)
                    val dao = database.checkLatestDao()
                    CoroutineScope(IO).launch {
                        dao.insertLatestVersion(checkLatestVersion.success)
                    }
                }
            }
        })

        CheckForLatest.checkForLatestUpdate(context)
        return Result.success()
    }

    object CheckForLatest {
        fun checkForLatestUpdate(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val checkForUpdates =
                OneTimeWorkRequestBuilder<CheckLatestAppVersion>()
                    .setInitialDelay(24, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()

            workManager.enqueueUniqueWork(CHECK_FOR_LATEST, ExistingWorkPolicy.REPLACE, checkForUpdates)
        }
    }
}