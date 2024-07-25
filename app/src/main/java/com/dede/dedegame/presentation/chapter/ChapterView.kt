package com.dede.dedegame.presentation.chapter

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
import androidx.core.content.ContextCompat
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.extension.slideDown
import com.dede.dedegame.extension.slideUp
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.widget.gridRecyclerview.GridRecyclerView
import com.dede.dedegame.presentation.widget.nestedWeb.NestedWebView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView


class ChapterView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private var tvChapterName: TextView? = null
    private var collapsingToolbar: CollapsingToolbarLayout? = null
    private lateinit var spListChapter: Spinner
    private var wvContent: NestedWebView? = null
    private val rcvOption by lazy { findViewById<GridRecyclerView>(R.id.rcvOption) }
    private val ivMoveTop by lazy { findViewById<View>(R.id.ivMoveTop) }
    private val appBarLayout by lazy { findViewById<AppBarLayout>(R.id.app_bar) }


    override fun onInitView() {
        super.onInitView()
        collapsingToolbar = findViewById(R.id.toolbar_layout)
        tvChapterName = findViewById(R.id.tvStoryNameDetail)
        spListChapter = findViewById(R.id.spListChapter)
        ivMoveTop.visibility = View.GONE
        rcvOption.visibility = View.GONE
        setupToolbar()

        collapsingToolbar?.setContentScrimColor(
            ContextCompat.getColor(
                context,
                android.R.color.white
            )
        )
        collapsingToolbar?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                android.R.color.white
            )
        )

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
                rcvOption.visibility = View.VISIBLE
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

        wvContent?.setGestureListener(object : NestedWebView.GestureListener {
            override fun onSingleTap() {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideDown()
                } else {
                    rcvOption.slideUp()
                }
            }

            override fun onDoubleTap() {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideDown()
                } else {
                    rcvOption.slideUp()
                }
            }

            override fun onScroll(distanceX: Float, distanceY: Float) {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideDown()
                }
            }

            override fun onLongPress() {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideDown()
                } else {
                    rcvOption.slideUp()
                }
            }

            override fun onScrollDistance(cumulativeDistanceY: Float) {
                if (cumulativeDistanceY > (DimensUtil.screenHeight(context) / 4)) {
                    ivMoveTop.visibility = View.VISIBLE;
                } else {
                    ivMoveTop.visibility = View.GONE;
                }
            }
        })

        ivMoveTop.setOnClickListener {
            wvContent?.evaluateJavascript("window.scrollTo({ top: 0, behavior: 'smooth' });", null)
            appBarLayout.setExpanded(true, true)
            rcvOption.slideUp()
        }
    }

    private fun setupToolbar() {
        val containerBack: View = findViewById(R.id.containerBack)
        val txtStartTitle: TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle: TextView = findViewById(R.id.txtCenterTitle)
        txtCenterTitle.text = "Chapter"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    fun fillDataToSpinner(chapterId: Int, chapters: List<Chapter>) {

        val listChapterName = chapters.map { chapter: Chapter -> chapter.title }

        val adapter = ArrayAdapter(
            context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            listChapterName
        )
        adapter.setDropDownViewResource(R.layout.item_dropdown)
        spListChapter.adapter = adapter

        spListChapter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                spListChapter.setSelection(position)
                mPresenter.executeCommand(chapters[position].id?.let { ChangeChapterCmd(it) })
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        if (chapterId == -1) {
            spListChapter.setSelection(0)
        } else {
            spListChapter.setSelection(chapters.indexOfFirst { it.id == chapterId })
        }
    }

    fun setStoryName(name: String) {
        tvChapterName?.text = name
    }

    fun loadChapterContent(storyUrl: String) {
        wvContent?.loadUrl(storyUrl)
    }

    class ChangeChapterCmd(val chapterId: Int) : ICommand {}
    class OnBackCmd() : ICommand
}


