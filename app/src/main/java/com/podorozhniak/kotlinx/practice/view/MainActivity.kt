package com.podorozhniak.kotlinx.practice.view

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.connection.broadcastreceiver.ConnectivityReceiver
import com.podorozhniak.kotlinx.practice.extensions.disableTooltip
import com.podorozhniak.kotlinx.practice.extensions.setupWithNavController

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    lateinit var bottomNavigationView: BottomNavigationView
    private var currentNavController: LiveData<NavController>? = null

    private val connectivityReceiver = ConnectivityReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.Theme_ZorroVPN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.disableTooltip()

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onNetworkConnectionChanged() {
        val connMgr =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            Toast.makeText(this, "Connected to network", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No connection to network", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        bottomNavigationView.itemIconTintList = null

        val navGraphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_web_view,
            R.navigation.nav_work_manager
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds,
            supportFragmentManager,
            R.id.main_nav_host_container,
            intent
        )

        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
            }
        }

        controller.observe(
            this,
            Observer { navController ->
                // Unregister the old one if it exists
                currentNavController?.value?.removeOnDestinationChangedListener(listener)
                // Add the listener to the new NavController
                navController.addOnDestinationChangedListener(listener)
            }
        )
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
