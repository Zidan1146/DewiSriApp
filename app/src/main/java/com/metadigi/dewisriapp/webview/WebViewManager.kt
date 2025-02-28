package com.metadigi.dewisriapp.webview

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import com.metadigi.dewisriapp.utils.NetworkUtils.isInternetAvailable

object WebViewManager {
    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView(webView: WebView): WebSettings {
        val webSettings = webView.settings

        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.userAgentString = "DewiSriApp"

        return webSettings
    }

    private var currentUrl: String? = null

    fun checkAndLoadPage(webView: WebView) {
        val url = if(!isInternetAvailable()) {
            "file:///android_asset/error/no_internet/index.html"
        } else {
            "https://dewisri.metadigi.id"
        }

        if(currentUrl != url) {
            currentUrl = url
            webView.loadUrl(url)
            Log.i("LoadedUrlChanged:", webView.url.toString())
        }
    }
}