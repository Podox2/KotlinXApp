package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.MULTIPLIED_VALUE_KEY
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.RANDOM_VALUE_KEY
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_NON_COROUTINE_WORK

//запускає роботу в бекграунд потоці
class NonCoroutineSecondWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val randomValue = inputData.getInt(RANDOM_VALUE_KEY, 0)
        val multipliedValue = randomValue * 2
        Thread.sleep(3_000)
        val data = workDataOf(MULTIPLIED_VALUE_KEY to multipliedValue)
        Log.d(TAG_NON_COROUTINE_WORK, "the multiplied value is $multipliedValue")
        return Result.success(data)
    }
}