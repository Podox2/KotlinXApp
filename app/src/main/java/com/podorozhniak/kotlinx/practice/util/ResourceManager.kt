package com.podorozhniak.kotlinx.practice.util

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.podorozhniak.kotlinx.practice.di.appContext

fun getDrawable(@DrawableRes drawableRes: Int?): Drawable? {
    if (drawableRes == null) return null
    return ContextCompat.getDrawable(appContext, drawableRes)
}