package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.podorozhniak.kotlinx.practice.connection.worker.MyWorkManager

class PeriodicWork(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(MyWorkManager.TAG_PERIODIC_WORK, "hi! next meet in about 15 minutes")
        return Result.success()
    }
}
