package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_PERIODIC_WORK

class PeriodicWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG_PERIODIC_WORK, "hi! next meet in about 15 minutes")
        return Result.success()
    }
}
