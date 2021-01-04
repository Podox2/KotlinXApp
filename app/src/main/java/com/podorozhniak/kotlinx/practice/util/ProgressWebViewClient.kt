package com.podorozhniak.kotlinx.practice.util

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.core.content.ContextCompat.startActivity
import com.podorozhniak.kotlinx.practice.extensions.setGone

class ProgressWebViewClient(private val progress: ProgressBar) : WebViewClient() {

    companion object {
        const val URL_DEEP_LINK_MARKET = "market://"
    }

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
        progress.setGone()
    }

    //handle play market's deep links
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if(url.contains(URL_DEEP_LINK_MARKET)) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(view.context, intent, null)
            return true
        }
        return false
    }
}