package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.KEY
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_SIMPLE_WORK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

//CoroutineWorker запускають роботу в корутині. можна легко змінювати потоки через withContext()
class SimpleWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val data = inputData.getString(KEY)
            Log.d(TAG_SIMPLE_WORK, "the data is $data")
            (0..9).forEach {
                Log.d(TAG_SIMPLE_WORK, "the number is $it")
                delay(1_000)
            }
        }
        return Result.success()
    }
}
