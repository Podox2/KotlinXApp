package com.podorozhniak.kotlinx.practice.view.fragment_result_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.podorozhniak.kotlinx.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SecondActivity_Theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportFragmentManager.beginTransaction().add(R.id.container, FirstFragment()).commit()
    }
}