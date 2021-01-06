package com.podorozhniak.kotlinx.practice.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.podorozhniak.kotlinx.practice.di.appContext

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    companion object {
        const val TAG_TEST = "TAG_TEST"
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

    protected fun toast(text: String) =
        Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()

    protected fun log(text: String) = Log.d(TAG_TEST, text)

}