package com.podorozhniak.kotlinx.practice.connection.worker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.messaging.RemoteMessage
import com.podorozhniak.kotlinx.KotlinXApp
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.KEY
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.RESULT
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.TAG_SIMPLE_WORK
import com.podorozhniak.kotlinx.practice.connection.worker.WorkManagerConstants.WORKER_INTENT
import com.podorozhniak.kotlinx.practice.util.NotificationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

//CoroutineWorker запускають роботу в корутині. можна легко змінювати потоки через withContext()
class SimpleWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        var result: String
        withContext(Dispatchers.IO) {
            val data = inputData.getString(KEY)
            Log.d(TAG_SIMPLE_WORK, "the data is $data")
            val api = (applicationContext as KotlinXApp).messageApi
            result = api!!.messages()[0].text
            delay(3_000)
        }
        //вставляєм респонс в bundle
        val bundle = Bundle()
        bundle.putString(RESULT, result)

        //intent для бродкасту з власним action і бандлом
        val intent = Intent(WORKER_INTENT)
        intent.putExtra(RESULT, bundle)
        //простіший варіант передати стрінгу
        //intent.putExtra(RESULT, result)

        //можна показати сповіщення як варіант обробки роботи
        //навіть якщо закрити аплікуху, сповіщення з'явиться (на емуляторі це відбувається з паузою)
        NotificationUtil.showNotification(RemoteMessage(bundle))

        //надсилаємо бродкаст, якщо хочемо так обробити результат (WorkManagerBroadcastReceiver)
        applicationContext.sendBroadcast(intent)

        //стандартний(?) варіант обробити результат через лайв дату (WorkManagerViewModel.outputWorkInfos)
        //при обробці лайв дати в WorkManagerFragment чомусь двічі приходить результат
        val outputData = workDataOf(RESULT to result)
        return Result.success(outputData)
    }
}