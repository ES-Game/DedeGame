package com.dede.dedegame.presentation.widget.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.util.AttributeSet
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dede.dedegame.R
import com.dede.dedegame.extension.toFacebookUriScheme
import com.dede.dedegame.presentation.common.LogUtil


@SuppressLint("SetJavaScriptEnabled")
class CustomWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {

    init {
        setBackgroundResource(R.drawable.background_app_common)
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
                    if (url.contains("facebook.com/groups")) {
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url.toFacebookUriScheme())
                            )
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(url)
                                )
                            )
                        }
                        return true
                    }
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                LogUtil.getInstance().e("Loading Start....")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtil.getInstance().e("Loading Finished....")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                LogUtil.getInstance().e("Loading Error: " + error.toString())
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                LogUtil.getInstance().e("Loading Error: " + errorResponse.toString())
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                LogUtil.getInstance().e("Loading Error SSL: " + error.toString())
            }
        }
    }

    fun loadHtml(htmlCode: String) {
        loadDataWithBaseURL(null, htmlCode, "text/html", "UTF-8", "about:blank")
    }
}