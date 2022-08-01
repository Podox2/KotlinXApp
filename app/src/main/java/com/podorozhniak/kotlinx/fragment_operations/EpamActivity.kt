package com.podorozhniak.kotlinx.fragment_operations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.podorozhniak.kotlinx.R

class EpamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epam)

        findViewById<Button>(R.id.btn_add_fragment).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, EpamFragment())
                .addToBackStack("epam fragment")
                .commit()
            Log.d("TAGGG", "before operation - ${supportFragmentManager.fragments.size}")
            Log.d("TAGGG", "-------------------------------------------------")
        }

        findViewById<Button>(R.id.btn_replace_fragment).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EpamSecondFragment())
                .addToBackStack("epam fragment")
                .commit()
            Log.d("TAGGG", "before operation - ${supportFragmentManager.fragments.size}")
            Log.d("TAGGG", "-------------------------------------------------")
        }
    }
}