package com.podorozhniak.kotlinx.practice.view.services.start

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.podorozhniak.kotlinx.practice.view.services.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    // спрацьовує, коли сервіс запущений методом startService
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        //someTask()

        val time = intent!!.getIntExtra("time", 1)
        val task = intent.getIntExtra("task", 0)

        thread {
            MyRun(startId, time, task).run()
        }

        //es.execute(myRun)
        /*START_NOT_STICKY – сервис не будет перезапущен после того, как был убит системой
        START_STICKY – сервис будет перезапущен после того, как был убит системой
        START_REDELIVER_INTENT – сервис будет перезапущен после того, как был убит системой.
        Кроме этого, сервис снова получит все вызовы startService, которые не были завершены методом stopSelf(startId).*/
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onBind(intent: Intent): IBinder? {
        log("onBind")
        return null
    }

    private fun someTask() {
        thread {
            for (i in 1..7) {
                log("i = $i")
                try {
                    TimeUnit.SECONDS.sleep(1)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            stopSelf()
        }
    }

    private fun log(text: String) {
        Log.d(SERVICE_LOG, text)
    }

    inner class MyRun(var startId: Int, var time: Int, var task: Int) : Runnable {

        init {
            log("MyRun#$startId create")
        }

        override fun run() {
            val intent = Intent(BROADCAST_ACTION)
            log("MyRun#$startId start, time = $time")
            try {
                // сообщаем о старте задачи
                intent.putExtra(PARAM_TASK, task)
                intent.putExtra(PARAM_STATUS, 100)
                sendBroadcast(intent)

                // начинаем выполнение задачи
                TimeUnit.SECONDS.sleep(time.toLong())

                // сообщаем об окончании задачи
                intent.putExtra(PARAM_STATUS, 200)
                intent.putExtra(PARAM_RESULT, time * 100)
                sendBroadcast(intent)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            stop()
        }

        private fun stop() {
            log(
                "MyRun#" + startId + " end, stopSelfResult("
                        + startId + ") = " + stopSelfResult(startId)
            )
        }
    }
}
