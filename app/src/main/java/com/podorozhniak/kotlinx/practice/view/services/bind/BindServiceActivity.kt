package com.podorozhniak.kotlinx.practice.view.services.bind

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.ActivityBindServiceBinding
import com.podorozhniak.kotlinx.practice.extensions.onClick

class BindServiceActivity : AppCompatActivity() {

    private val BIND_SERVICE_LOG = "bind_service_log"
    private var isBound = false
    lateinit var serviceConnection: ServiceConnection
    var interval = 0L
    lateinit var serviceToBind: ServiceToBind
    //lateinit var intent: Intent

    lateinit var binding: ActivityBindServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_KotlinX)
        super.onCreate(savedInstanceState)
        binding = ActivityBindServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //якщо сервіс в іншій аплікусі, наприклад
        /*val intent = Intent("BIND_SERVICE_ACTION_1447")
        intent.setPackage("com.podorozhniak.kotlinx")*/
        val intent = Intent(this, ServiceToBind::class.java)

        serviceConnection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                log(name.toString())
                log("MainActivity onServiceConnected")
                serviceToBind = (service as ServiceToBind.MyBinder).getService()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                log(name.toString())
                log("MainActivity onServiceDisconnected")
                isBound = false
            }
        }

        binding.apply {
            btnStart.onClick {
                startService(intent)
            }
            btnStop.onClick {
                stopService(intent)
            }
            btnBind.onClick {
                bindService(intent, serviceConnection, 0)
            }
            btnUnBind.onClick {
                if(!isBound) return@onClick
                unbindService(serviceConnection)
                isBound = false
            }
            btnUp.onClick {
                if (!isBound) return@onClick
                interval = serviceToBind.upInterval(500)
                tvInterval.text = "interval = $interval"
            }
            btnDown.onClick {
                if (!isBound) return@onClick
                interval = serviceToBind.downInterval(500)
                tvInterval.text = "interval = $interval"
            }
        }
    }

    private fun log(text: String) {
        Log.d(BIND_SERVICE_LOG, text)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!isBound) return
        unbindService(serviceConnection)
        isBound = false
    }
}