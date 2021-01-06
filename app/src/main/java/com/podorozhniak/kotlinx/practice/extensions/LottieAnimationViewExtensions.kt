package com.podorozhniak.kotlinx.practice.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath

fun LottieAnimationView.animate(animate: Boolean) {
    if (animate) {
        visibility = View.VISIBLE
        playAnimation()
    } else {
        visibility = View.GONE
        cancelAnimation()
    }
}

fun LottieAnimationView.setColor(context: Context, color: Int){
    this.addValueCallback(
        KeyPath("**"),
        LottieProperty.COLOR_FILTER,
        {
            PorterDuffColorFilter(
                ContextCompat.getColor(context, color),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    )
}