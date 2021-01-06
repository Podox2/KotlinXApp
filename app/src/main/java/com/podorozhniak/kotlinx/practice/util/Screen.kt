package com.podorozhniak.kotlinx.practice.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import org.koin.java.KoinJavaComponent.get
import kotlin.math.roundToInt

object Screen {
    fun convertDpToPx(dp: Float) : Float = dp * Resources.getSystem().displayMetrics.density

    fun convertPxToDp(px: Float) : Float = px / Resources.getSystem().displayMetrics.density

    fun getDisplayDensityViaContext(context: Context) = context.resources.displayMetrics.density
    fun getDisplayDensity() = Resources.getSystem().displayMetrics.density

    //same extension in ActivityExtensions.kt
    fun getDisplayHeightDp(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        return dpHeight.roundToInt()
    }
    //same extension in ActivityExtensions.kt
    fun getDisplayWidthDp(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return dpWidth.toInt()
    }

    /*fun isTablet(context: Context? = null): Boolean {
        val mContext: Context = context ?: get(Context::class.java)

        val result: Boolean? =
            com.google.android.gms.common.util.DeviceProperties.isTablet(mContext.resources)

        return result ?: ((mContext.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }*/
}
