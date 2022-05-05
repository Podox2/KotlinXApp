package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.RANDOM_VALUE_KEY
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_NON_COROUTINE_WORK

//запускає роботу в бекграунд потоці
class NonCoroutineWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val randomValue = (1..10).shuffled().first()
        val data = workDataOf(RANDOM_VALUE_KEY to randomValue)
        Log.d(TAG_NON_COROUTINE_WORK, "the random value is $randomValue")
        return Result.success(data)
    }
}