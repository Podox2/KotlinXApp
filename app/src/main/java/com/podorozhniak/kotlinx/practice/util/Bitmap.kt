package com.podorozhniak.kotlinx.practice.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

fun createFullScreenBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val displayWidth = Screen.getDisplayWidthDp(context)
    val displayHeight =
        Screen.getDisplayHeightDp(context) + Screen.getDisplayDensityViaContext(context).roundToInt()
    val bitmap = Bitmap.createBitmap(
        displayWidth,
        displayHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}