package com.podorozhniak.kotlinx.practice.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.connection.broadcastreceiver.ConnectivityReceiver
import com.podorozhniak.kotlinx.practice.extensions.disableTooltip
import com.podorozhniak.kotlinx.practice.extensions.setupWithNavController
import com.podorozhniak.kotlinx.practice.util.Screen
import com.podorozhniak.kotlinx.practice.view.secondactivity.SecondActivity

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    lateinit var bottomNavigationView: BottomNavigationView
    private var currentNavController: LiveData<NavController>? = null

    private val connectivityReceiver = ConnectivityReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_KotlinX)
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val cutout = window.decorView.rootWindowInsets.displayCutout
            if (cutout != null) {
                try {
                    val boundingRects = cutout.boundingRects
                    Screen.screenCutout = boundingRects[0].height()
                } catch (e: Exception) {
                }
            }
        } /*else {
            val cutout =
                WindowInsetsCompat.toWindowInsetsCompat(window.decorView.rootWindowInsets).displayCutout
            if (cutout != null) {
                try {
                    val boundingRects = cutout.boundingRects
                    BitmapCreator.screenCutout = boundingRects[0].height()
                } catch (e: Exception) {
                }
            }
        }*/
    }

    fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    fun changeNavBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.dark)
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
        /*remove default colors for icons (even if tint set in layout)
          colors from selector (ic_tab_selector) will be used*/
        bottomNavigationView.itemIconTintList = null

        val navGraphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_coroutines,
            R.navigation.nav_work_manager,
            R.navigation.nav_web_view
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
        //return currentNavController?.value?.navigateUp() ?: false
        return Navigation.findNavController(this, R.id.main_nav_host_container).navigateUp()
                || super.onSupportNavigateUp()
    }

    fun openSecondActivity() {
        startActivity(
            Intent(
                this, SecondActivity::class.java
            )
        )
    }
}
