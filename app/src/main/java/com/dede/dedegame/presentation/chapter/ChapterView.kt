package com.quangph.dedegame.presentation.chapter

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.quangph.base.mvp.ICommand
import com.dede.dedegame.R
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.dedegame.domain.model.Chapter

class ChapterView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private var tvChapterName: TextView? = null
    private lateinit var spListChapter: Spinner
    private var wvContent: WebView? = null

    override fun onInitView() {
        super.onInitView()
        tvChapterName = findViewById(R.id.tvStoryNameDetail)

        spListChapter = findViewById(R.id.spListChapter)
        spListChapter.setSelection(0)

        wvContent = findViewById(R.id.wvContent)
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

    fun fillDataToSpinner(chapters: List<Chapter>) {

        val listChapterName = chapters.map { chapter: Chapter -> chapter.title }

        val adapter = ArrayAdapter(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listChapterName)
        adapter.setDropDownViewResource(R.layout.item_dropdown)
        spListChapter.adapter = adapter

        spListChapter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                spListChapter.setSelection(position)
                mPresenter.executeCommand(chapters[position].id?.let { ChangeChapterCmd(it) })
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    fun setStoryName(name: String) {
        tvChapterName?.text = name
    }

    fun loadChapterContent(storyUrl: String) {
        wvContent?.loadUrl(storyUrl)
    }

    class ChangeChapterCmd(val chapterId: Int): ICommand {}
}


