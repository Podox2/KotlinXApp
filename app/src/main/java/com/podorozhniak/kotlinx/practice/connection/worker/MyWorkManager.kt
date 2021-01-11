package com.podorozhniak.kotlinx.practice.connection.worker

import android.util.Log
import androidx.work.*
import com.podorozhniak.kotlinx.practice.di.appContext
import java.util.concurrent.TimeUnit

class MyWorkManager {
    companion object {
        const val TAG_SIMPLE_WORK = "TAG_SIMPLE_WORK"
        const val TAG_PERIODIC_WORK = "TAG_PERIODIC_WORK"
        const val KEY = "KEY"
        private const val CHECK_VPN_INTERVAL_MIN = 15

        fun startSimpleWork() {
            val data = workDataOf(KEY to "value")
            Log.d(TAG_SIMPLE_WORK, "startSimpleWork")
            val oneTimeWorkRequest = OneTimeWorkRequest
                .Builder(SimpleWork::class.java)
                .addTag(TAG_SIMPLE_WORK)
                .setInputData(data)
                .build()

            WorkManager.getInstance(appContext).enqueue(oneTimeWorkRequest)
        }

        fun stopSimpleWork() {
            Log.d(TAG_SIMPLE_WORK, "stopSimpleWork")
            WorkManager.getInstance(appContext).cancelAllWorkByTag(TAG_SIMPLE_WORK)
        }

        fun startPeriodicWork() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val periodicWorkRequest = PeriodicWorkRequest.Builder(
                PeriodicWork::class.java,
                (CHECK_VPN_INTERVAL_MIN + 1).toLong(),
                TimeUnit.MINUTES,
                CHECK_VPN_INTERVAL_MIN.toLong(),
                TimeUnit.MINUTES
            )
                .addTag(TAG_PERIODIC_WORK)
                .setConstraints(constraints)
                .build()
            Log.d(TAG_PERIODIC_WORK, "startPeriodicWork")
            WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
                TAG_PERIODIC_WORK,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
        }

        fun stopPeriodicWork() {
            Log.d(TAG_PERIODIC_WORK, "stopPeriodicWork")
            WorkManager.getInstance(appContext).cancelAllWorkByTag(TAG_PERIODIC_WORK)
        }
    }
}
