package com.podorozhniak.kotlinx.practice.base

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.podorozhniak.kotlinx.practice.di.appContext

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    companion object {
        const val TAG_TEST = "TAG_TEST"
        const val FLASH_ANIMATION_DURATION = 500L
    }

    abstract val layoutId: Int
    lateinit var binding: B

    abstract fun onCreateViewBinding(view: View): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = onCreateViewBinding(inflater.inflate(layoutId, container, false))
        return binding.root
    }

    protected fun handleBackWithWebView(webView: WebView) {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            findNavController().navigateUp()
        }
    }

    protected fun showFlashAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeOut.duration = FLASH_ANIMATION_DURATION
        fadeIn.duration = FLASH_ANIMATION_DURATION
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {
                binding.root.visibility = View.INVISIBLE
            }
        })
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                binding.root.visibility = View.VISIBLE
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
        binding.root.apply {
            startAnimation(fadeOut)
            startAnimation(fadeIn)
        }
    }

    //screenshot?
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getBitmapFromView(view: View, activity: Activity, callback: (Bitmap) -> Unit) {
        activity.window?.let { window ->
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)
            try {
                PixelCopy.request(
                    window, Rect(
                        locationOfViewInWindow[0],
                        locationOfViewInWindow[1],
                        locationOfViewInWindow[0] + view.width,
                        locationOfViewInWindow[1] + view.height
                    ), bitmap, { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            callback(bitmap)
                        }
                    }, Handler()
                )
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    protected fun toast(text: String) =
        Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()

    protected fun log(text: String) = Log.d(TAG_TEST, text)
}