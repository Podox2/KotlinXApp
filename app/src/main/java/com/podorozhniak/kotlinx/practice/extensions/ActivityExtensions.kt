package com.podorozhniak.kotlinx.practice.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.podorozhniak.kotlinx.practice.util.Screen

/*
extension для скрытия Software Keyboard
 */
fun Activity.hideKeyboard() {
    val inputManager: InputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
}

fun Activity.heightScreenInPx(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowManager.currentWindowMetrics.bounds.height()
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

//same method in Screen.kt
fun Activity.heightScreenInDp(): Int {
    return Screen.convertPxToDp(this.heightScreenInPx().toFloat()).toInt()
}

fun Activity.widthScreenInPx(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowManager.currentWindowMetrics.bounds.height()
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

//same method in Screen.kt
fun Activity.widthScreenInDp(): Int {
    return Screen.convertPxToDp(this.widthScreenInPx().toFloat()).toInt()
}

/*
extension для проверки, открыта или нет Software Keyboard
 */
fun Activity.isKeyboardOpen(): Boolean {
    val size = this.getDisplaySizeInPx()
    val root = this.findViewById<ViewGroup>(android.R.id.content)
    val rect = Rect()
    root.getWindowVisibleDisplayFrame(rect)
    return rect.height() < (size.y - rect.top * 2)
}

fun Activity.makeStatusBarTransparent() {
    window.apply {
        // remove translucent flag from the
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.animationStatusBarColor(colorFromId: Int, colorToId: Int) {
    window.apply {
        val colorFrom = ContextCompat.getColor(context, colorFromId)
        val colorTo = ContextCompat.getColor(context, colorToId)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 2000
        colorAnimation.addUpdateListener { animator ->
            statusBarColor = animator.animatedValue as Int
        }
        colorAnimation.start()
    }
}

// вертає root layout activity
fun Activity.getActivityRoot(): View {
    return (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
}

//вертає пару значень а-ля Point(1080, 2134)
fun Activity.getDisplaySizeInPx(): Point {
    val display = this.windowManager.defaultDisplay
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display = appContext.display
    }*/
    val size = Point()
    display.getSize(size)
    return size
}

fun Activity.getDrawableIdByName(drawableName: String) =
    this.applicationContext.resources.getIdentifier(
        drawableName,
        "drawable",
        this.applicationContext.packageName
    )

fun Activity.getRawIdByName(rawName: String) =
    this.applicationContext.resources.getIdentifier(
        rawName,
        "raw",
        this.applicationContext.packageName
    )


