package com.podorozhniak.kotlinx.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {
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

}