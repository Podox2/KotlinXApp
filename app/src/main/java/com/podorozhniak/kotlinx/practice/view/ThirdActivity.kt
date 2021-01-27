package com.podorozhniak.kotlinx.practice.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.extensions.onClick

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SecondActivity_Theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        findViewById<TextView>(R.id.tv_third_activity).onClick {
            sendData()
        }
    }

    private fun sendData() {
        val intent = intent.putExtra("key", "Data from Third Activity")
        setResult(RESULT_OK, intent)
        finish()
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}