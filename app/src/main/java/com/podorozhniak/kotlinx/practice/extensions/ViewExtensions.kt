package com.podorozhniak.kotlinx.practice.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val CLICK_DELAY_MS = 10L
const val DEFAULT_ALPHA_ON_CLICK_WITH_EFFECT = 0.5f

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setPaddingTop(paddingTop: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

fun View.setMarginTop(margin: Int) {
    val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    marginLayoutParams.topMargin = margin
    layoutParams = marginLayoutParams
    invalidate()
}

fun View.setMarginBottom(margin: Int) {
    val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    marginLayoutParams.bottomMargin = margin
    layoutParams = marginLayoutParams
    invalidate()
}

fun View.setPaddingBottom(paddingBottom: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

fun View.setAnimationColor(colorFromId: Int, colorToId: Int) {
    val colorFrom = ContextCompat.getColor(context, colorFromId)
    val colorTo = ContextCompat.getColor(context, colorToId)
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 250
    colorAnimation.addUpdateListener { animator -> this.setBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()
}

fun View.removeFocusable() {
    this.isFocusableInTouchMode = false
    this.isFocusable = false
    this.isFocusableInTouchMode = true
    this.isFocusable = true
}

fun View.fadeOutAnimation(duration: Long = 400) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = View.INVISIBLE
        }
}

fun View.fadeInAnimation(duration: Long = 300) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f).duration = duration
}

inline fun View.addOnPressedSelector(
    crossinline doOnPressed: () -> Unit = {},
    crossinline doOtherwise: () -> Unit = {}
): View {
    viewTreeObserver.addOnDrawListener {
        if (this.isPressed) {
            doOnPressed()
        } else {
            doOtherwise()
        }
    }
    return this
}

/**
 * Safe onClick listener
 */
fun <T : View> T?.onClick(
    scope: CoroutineScope = MainScope(),
    delayMs: Long = CLICK_DELAY_MS,
    action: (T) -> Unit
) {
    this?.let { view ->
        callbackFlow {
            view.setOnClickListener {
                offer(Unit)
            }
            awaitClose {
                view.setOnClickListener(null)
            }
        }.debounce(delayMs).onEach {
            action(view)
        }.launchIn(scope)
    }
}




