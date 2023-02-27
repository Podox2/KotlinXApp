package com.podorozhniak.kotlinx.practice.view

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.broadcastreceiver.ConnectivityReceiver
import com.podorozhniak.kotlinx.practice.broadcastreceiver.WorkManagerBroadcastReceiver
import com.podorozhniak.kotlinx.practice.workmanager.WorkManagerConstants.WORKER_INTENT
import com.podorozhniak.kotlinx.practice.extensions.disableTooltip
import com.podorozhniak.kotlinx.practice.extensions.setupWithNavController
import com.podorozhniak.kotlinx.practice.util.Screen
import com.podorozhniak.kotlinx.practice.view.fragment_result_api.SecondActivity
import com.podorozhniak.kotlinx.practice.view.services.bind.BindServiceActivity
import com.podorozhniak.kotlinx.practice.view.services.start.ServiceActivity
import com.podorozhniak.kotlinx.theory.reified.startActivity

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener,
    WorkManagerBroadcastReceiver.WorkManagerBroadcastHandler {

    lateinit var bottomNavigationView: BottomNavigationView
    private var currentNavController: LiveData<NavController>? = null

    private val connectivityReceiver = ConnectivityReceiver()
    private val workManagerReceiver = WorkManagerBroadcastReceiver()

    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val dataValue = data?.getStringExtra("key")
            Toast.makeText(this, dataValue, Toast.LENGTH_SHORT).show()
        }
    }

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
        workManagerReceiver.broadcastHandler = this
        //реєструємо якими ресіверами обробляти які інтенти
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        registerReceiver(
            workManagerReceiver,
            IntentFilter(WORKER_INTENT)
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
            //Toast.makeText(this, "Connected to network", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No connection to network", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleBroadcast(response: String) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
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
            R.navigation.nav_flow,
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
        // old boring approach
        /*startActivity(
            Intent(
                this, SecondActivity::class.java
            )
        )*/
        // cool modern Kotlin-way reified extension fun to start Activity
        this.startActivity<SecondActivity>()
        //this.overridePendingTransition(R.anim.slide_up, R.anim.wait)
    }

    fun openThirdActivityForResult() {
        val intent = Intent(this, ThirdActivity::class.java)
        resultLauncher.launch(intent)
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun openServiceActivity() {
        startActivity(
            Intent(
                this, ServiceActivity::class.java
            )
        )
    }

    fun openBindServiceActivity() {
        startActivity(
            Intent(
                this, BindServiceActivity::class.java
            )
        )
    }
}
