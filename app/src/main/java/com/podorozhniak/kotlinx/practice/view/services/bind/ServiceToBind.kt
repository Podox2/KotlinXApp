package com.podorozhniak.kotlinx.practice.view.services.bind

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

//https://startandroid.ru/ru/uroki/vse-uroki-spiskom/163-urok-98-service-lokalnyj-binding.html
class ServiceToBind : Service() {

    private val BIND_SERVICE_LOG = "bind_service_log"

    private val binder = MyBinder()

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var interval = 1_000L

    override fun onCreate() {
        super.onCreate()
        log("ServiceToBind onCreate")
        timer = Timer()
        schedule()
    }

    private fun schedule() {
        if (timerTask != null) timerTask?.cancel()
        if (timer == null) timer = Timer()
        if (interval > 0) {
            timerTask = object: TimerTask() {
                override fun run() {
                    log("run")
                }
            }
            timer?.schedule(timerTask, 1_000, interval)
        }
    }

    fun upInterval(gap: Long): Long {
        interval += gap
        schedule()
        return interval
    }

    fun downInterval(gap: Long): Long {
        interval -= gap
        if (interval < 0) interval = 0
        schedule()
        return interval
    }

    override fun onBind(intent: Intent?): IBinder {
        log("ServiceToBind onBind")
        return binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        log("ServiceToBind onRebind")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        log("ServiceToBind onUnbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        log("ServiceToBind onDestroy")
    }

    private fun log(text: String) {
        Log.d(BIND_SERVICE_LOG, text)
    }

    inner class MyBinder : Binder() {
        fun getService() = ServiceToBind()
    }

}