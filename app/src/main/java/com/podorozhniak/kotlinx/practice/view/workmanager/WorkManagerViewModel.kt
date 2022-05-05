package com.podorozhniak.kotlinx.practice.view.workmanager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import com.podorozhniak.kotlinx.practice.connection.worker.NonCoroutineSecondWorker
import com.podorozhniak.kotlinx.practice.connection.worker.NonCoroutineWorker
import com.podorozhniak.kotlinx.practice.connection.worker.PeriodicWorker
import com.podorozhniak.kotlinx.practice.connection.worker.SimpleWorker
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.INTERVAL_MINIMUM
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.KEY
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.NON_COROUTINE_WORK_NAME
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_NON_COROUTINE_WORK
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_PERIODIC_WORK
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_SIMPLE_WORK
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.VALUE
import java.util.concurrent.TimeUnit

class WorkManagerViewModel(application: Application) : AndroidViewModel(application) {

    private val myWorkManager = WorkManager.getInstance(application)
    val outputWorkInfos: LiveData<List<WorkInfo>> =
        myWorkManager.getWorkInfosByTagLiveData(TAG_PERIODIC_WORK)

    //запускає просту роботу
    fun startSimpleWork() {
        //об'єкт для передачі даних для роботи
        val data = workDataOf(KEY to VALUE)
        Log.d(TAG_SIMPLE_WORK, "start simple work")

        val oneTimeWorkRequest = OneTimeWorkRequest
            .Builder(SimpleWorker::class.java)
            .addTag(TAG_SIMPLE_WORK)
            .setInputData(data)
            .build()

        //ktx варіант білдера
        val oneTimeWorkRequestKtxBuilder = OneTimeWorkRequestBuilder<SimpleWorker>()
            .addTag(TAG_SIMPLE_WORK)
            .setInputData(data)
            .build()

        myWorkManager.enqueue(oneTimeWorkRequest)
        //інший варіант запуску роботи
        myWorkManager.beginWith(oneTimeWorkRequest).enqueue()
    }

    fun stopSimpleWork() {
        Log.d(TAG_SIMPLE_WORK, "stop simple work")
        myWorkManager.cancelAllWorkByTag(TAG_SIMPLE_WORK)
    }

    //запускає періодичну роботу
    fun startPeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicWorker::class.java,
            (INTERVAL_MINIMUM + 1).toLong(),
            TimeUnit.MINUTES,
            INTERVAL_MINIMUM.toLong(),
            TimeUnit.MINUTES
        )
            .addTag(TAG_PERIODIC_WORK)
            .setConstraints(constraints)
            .build()

        Log.d(TAG_PERIODIC_WORK, "start periodic work")

        myWorkManager.enqueueUniquePeriodicWork(
            TAG_PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    fun stopPeriodicWork() {
        Log.d(TAG_PERIODIC_WORK, "stop periodic work")
        myWorkManager.cancelAllWorkByTag(TAG_PERIODIC_WORK)
    }

    //запускає дві роботи, які виконуються одна за одною
    //результат першої роботи буде переданий в наступну роботу
    fun startChainOfWorks() {
        Log.d(TAG_NON_COROUTINE_WORK, "start non coroutine work")

        val nonCoroutineWorkRequest = OneTimeWorkRequestBuilder<NonCoroutineWorker>()
            .addTag(TAG_NON_COROUTINE_WORK)
            .build()

        val nonCoroutineSecondWorkRequest = OneTimeWorkRequestBuilder<NonCoroutineSecondWorker>()
            .addTag(TAG_NON_COROUTINE_WORK)
            .build()

        myWorkManager
            .beginUniqueWork(
                NON_COROUTINE_WORK_NAME,
                ExistingWorkPolicy.KEEP, //REPLACE, APPEND, APPEND_OR_REPLACE
                nonCoroutineWorkRequest
            )
            .then(nonCoroutineSecondWorkRequest)
            .enqueue()

        //запускає дві роботи паралельно
        /*myWorkManager
            .beginWith(
                listOf(
                    nonCoroutineWorkRequest, nonCoroutineSecondWorkRequest
                )
            )
            .enqueue()*/
    }

}