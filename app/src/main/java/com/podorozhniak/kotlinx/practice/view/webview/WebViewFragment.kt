package com.podorozhniak.kotlinx.practice.view.webview

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentWebviewBinding
import com.podorozhniak.kotlinx.practice.BaseFragment
import com.podorozhniak.kotlinx.practice.util.ProgressWebViewClient

class WebViewFragment : BaseFragment<FragmentWebviewBinding>() {

    companion object {
        const val WEB_LINK = "https://uk.wikipedia.org/wiki/Квітка"
    }

    override val layoutId: Int
        get() = R.layout.fragment_webview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackWithWebView(binding.webView)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateViewBinding(view: View): FragmentWebviewBinding =
        FragmentWebviewBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply {
            webViewClient = ProgressWebViewClient(binding.progress)
            loadUrl(WEB_LINK)
        }
    }
}