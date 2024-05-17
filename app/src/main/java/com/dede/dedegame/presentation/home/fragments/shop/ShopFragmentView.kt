package com.dede.dedegame.presentation.home.fragments.shop

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.util.Log
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class ShopFragmentView(context: Context?, attrs: AttributeSet?) :
    BaseConstraintView(context, attrs) {

    private val wvContent by lazy { findViewById<WebView>(R.id.wvContent) }

    override fun onInitView() {
        super.onInitView()
        if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!){
            mPresenter.executeCommand(OnGetPaymentLinkCmd())
            initWebView()
        } else {
            mPresenter.executeCommand(OnLogoutCmd())
        }
    }

    private fun initWebView() {
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().acceptThirdPartyCookies(wvContent)
        wvContent?.settings?.javaScriptEnabled = true
        wvContent?.settings?.loadWithOverviewMode = true
        wvContent?.settings?.useWideViewPort = true
        wvContent?.settings?.domStorageEnabled = true
        wvContent?.webViewClient = object : WebViewClient() {
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

    fun loadPaymentContent(storyUrl: String) {
        wvContent?.loadUrl(storyUrl)
    }

    class OnLogoutCmd() : ICommand
    class OnGetPaymentLinkCmd() : ICommand
}


