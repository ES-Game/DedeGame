package com.dede.dedegame.presentation.widget

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {
    
    init {
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().acceptThirdPartyCookies(this)
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.domStorageEnabled = true
        webViewClient = WebViewClient()
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.i("Loading Start....", "")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.i("Loading Finished....", "")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e("Loading Error: ", error.toString())
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("Loading Error: ", errorResponse.toString())
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                Log.e("Loading Error SSL: ", error.toString())
            }
        }
    }

    private var isScrollable = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (isScrollable) {
            super.onTouchEvent(event)
        } else {
            // Only handle click events, ignore scrolling events
            when (event?.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> super.onTouchEvent(event)
                else -> true
            }
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        if (isScrollable) {
            super.onScrollChanged(l, t, oldl, oldt)
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        if (isScrollable) {
            super.scrollTo(x, y)
        }
    }

    fun setScrollingEnabled(enabled: Boolean) {
        isScrollable = enabled
    }

    fun loadHtml(htmlCode: String) {
        loadDataWithBaseURL(null, htmlCode, "text/html", "UTF-8", null)
    }
}