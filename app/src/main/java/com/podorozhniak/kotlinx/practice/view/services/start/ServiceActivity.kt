package com.podorozhniak.kotlinx.practice.view.services.start

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.ActivityServiceBinding
import com.podorozhniak.kotlinx.practice.extensions.onClick
import com.podorozhniak.kotlinx.practice.view.services.*


class ServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceBinding
    private val serviceBroadcastReceiver = ServiceBroadcastReceiver(::onServiceWorkChanged)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_KotlinX)
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnStart.onClick {
                //startService(Intent(this@ServiceActivity, MyService::class.java))
                startService()
            }

            btnStop.onClick {
                stopService(Intent(this@ServiceActivity, MyService::class.java))
            }

            btnTest.onClick {
                Log.d(SERVICE_LOG, "test")
            }
        }
    }

    private fun onServiceWorkChanged(task: Int, status: Int, result: Int) {
        Log.d(SERVICE_LOG, "onReceive: task = $task, status = $status")

        // Ловим сообщения о старте задач
        if (status == STATUS_START) {
            when (task) {
                TASK1_CODE -> binding.tvTask1.text = "Task1 start"
                TASK2_CODE -> binding.tvTask2.text = "Task2 start"
                TASK3_CODE -> binding.tvTask3.text = "Task3 start"
            }
        }

        // Ловим сообщения об окончании задач
        if (status == STATUS_FINISH) {
            when (task) {
                TASK1_CODE -> binding.tvTask1.text = "Task1 finish, result = $result"
                TASK2_CODE -> binding.tvTask2.text = "Task2 finish, result = $result"
                TASK3_CODE -> binding.tvTask3.text = "Task3 finish, result = $result"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            serviceBroadcastReceiver,
            IntentFilter(BROADCAST_ACTION)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(serviceBroadcastReceiver)
    }

    private fun startService() {
        // Создаем Intent для вызова сервиса,
        // кладем туда параметр времени и код задачи
        val intent1: Intent = Intent(this, MyService::class.java)
            .putExtra(PARAM_TIME, 7)
            .putExtra("task", TASK1_CODE)
        startService(intent1)

        val intent2 = Intent(this, MyService::class.java)
            .putExtra(PARAM_TIME, 4)
            .putExtra(PARAM_TASK, TASK2_CODE)
        startService(intent2)

        val intent3 = Intent(this, MyService::class.java)
            .putExtra(PARAM_TIME, 6)
            .putExtra(PARAM_TASK, TASK3_CODE)
        startService(intent3)
    }
}