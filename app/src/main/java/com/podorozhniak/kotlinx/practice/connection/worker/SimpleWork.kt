package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.podorozhniak.kotlinx.practice.connection.worker.MyWorkManager.Companion.KEY
import kotlinx.coroutines.delay

class SimpleWork(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val data = inputData.getString(KEY)
        (0..9).forEach {
            Log.d(MyWorkManager.TAG_SIMPLE_WORK, "the number is $it")
            delay(1_000)
        }
        return Result.success()
    }
}
