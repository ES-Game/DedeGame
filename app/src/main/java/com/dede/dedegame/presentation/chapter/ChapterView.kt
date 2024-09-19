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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.util.forEach
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.OptionChapter
import com.dede.dedegame.domain.model.TypeOption
import com.dede.dedegame.extension.oldIndexOfChapter
import com.dede.dedegame.extension.slideEnd
import com.dede.dedegame.extension.slideStart
import com.dede.dedegame.presentation.chapter.adapter.ChapterSpinnerAdapter
import com.dede.dedegame.presentation.chapter.group.ListChapterNavGroupData
import com.dede.dedegame.presentation.chapter.group.OptionChapterGroupData
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.widget.nestedWeb.NestedWebView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseDrawerLayout
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter
import com.skydoves.powerspinner.PowerSpinnerView


class ChapterView(context: Context?, attrs: AttributeSet?) : BaseDrawerLayout(context!!, attrs) {

    private var tvChapterName: TextView? = null
    private var collapsingToolbar: CollapsingToolbarLayout? = null
    private var wvContent: NestedWebView? = null
    private val tvChapterLabel by lazy { findViewById<TextView>(R.id.tvChapterLabel) }
    private val spListChapter by lazy { findViewById<PowerSpinnerView>(R.id.spListChapter) }
    private val rcvOption by lazy { findViewById<RecyclerView>(R.id.rcvOption) }
//    private val ivMoveTop by lazy { findViewById<View>(R.id.ivMoveTop) }
    private val appBarLayout by lazy { findViewById<AppBarLayout>(R.id.app_bar) }
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val rcvChapterNav by lazy { findViewById<RecyclerView>(R.id.rcvChapterNav) }


    private val mMenuAdapter = GroupRclvAdapter()
    private val optionChapterGroupData = OptionChapterGroupData(null)
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var chapterSpinnerAdapter: ChapterSpinnerAdapter

    private val mChapterNavAdapter = GroupRclvAdapter()
    private val mListChapterNavGroupData = ListChapterNavGroupData(null)
    private lateinit var mNavLayoutManager: LinearLayoutManager

    override fun onInitView() {
        super.onInitView()
        collapsingToolbar = findViewById(R.id.toolbar_layout)
        tvChapterName = findViewById(R.id.tvStoryNameDetail)
//        ivMoveTop.visibility = View.GONE
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
                    rcvOption.slideEnd()
                } else {
                    rcvOption.slideStart()
                }
            }

            override fun onDoubleTap() {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideEnd()
                } else {
                    rcvOption.slideStart()
                }
            }

            override fun onScroll(distanceX: Float, distanceY: Float) {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideEnd()
                }
            }

            override fun onLongPress() {
                if (rcvOption.visibility == View.VISIBLE) {
                    rcvOption.slideEnd()
                } else {
                    rcvOption.slideStart()
                }
            }

            override fun onScrollDistance(cumulativeDistanceY: Float) {
                if (cumulativeDistanceY > (DimensUtil.screenHeight(context) / 4)) {
//                    ivMoveTop.visibility = View.VISIBLE
                } else {
//                    ivMoveTop.visibility = View.GONE
                }
            }
        })

//        ivMoveTop.setOnClickListener {
//            wvContent?.evaluateJavascript("window.scrollTo({ top: 0, behavior: 'smooth' });", null)
//            appBarLayout.setExpanded(true, true)
//            rcvOption.slideStart()
//        }

        setupChapterNav()

        setupBottomMenu()
    }

    private fun setupChapterNav() {
        mNavLayoutManager = LinearLayoutManager(context)
        rcvChapterNav.layoutManager = mNavLayoutManager
        rcvChapterNav.adapter = mChapterNavAdapter
        mChapterNavAdapter.addGroup(mListChapterNavGroupData)
        mListChapterNavGroupData.onClickItemListener =
            object : ListChapterNavGroupData.OnClickItemListener {
                override fun onClickMenuItem(item: Chapter, position: Int) {
                    drawerLayout.closeDrawer(GravityCompat.END)
                    val oldIndex =
                        mListChapterNavGroupData.getItemStateArray().oldIndexOfChapter() ?: return
                    if (oldIndex != position) {
                        mListChapterNavGroupData.getItemStateArray().put(oldIndex, false)
                        mListChapterNavGroupData.getItemStateArray().put(position, true)

                        spListChapter.clearSelectedItem()
                        chapterSpinnerAdapter.index = position
                        mPresenter.executeCommand(item.id?.let { ChangeChapterCmd(it) })
                        spListChapter.setSpinnerAdapter(chapterSpinnerAdapter)
                        spListChapter.selectItemByIndex(position)
                        spListChapter.dismiss()
                        tvChapterLabel.text = item.title
                        fillDataToNavChapter(position, chapterSpinnerAdapter.items)
                        mPresenter.executeCommand(
                            RefreshMenuCmd(
                                optionChapterGroupData.menuChapter()!!,
                                chapterSpinnerAdapter.items,
                                chapterSpinnerAdapter.items[position].id!!
                            )
                        )
                        wvContent?.evaluateJavascript(
                            "window.scrollTo({ top: 0, behavior: 'smooth' });",
                            null
                        )
                        appBarLayout.setExpanded(true, true)
                        rcvOption.slideStart()
                    }
                }
            }
    }

    private fun setupBottomMenu() {
        mLayoutManager = LinearLayoutManager(context)
        rcvOption.layoutManager = mLayoutManager
        rcvOption.adapter = mMenuAdapter
        mMenuAdapter.addGroup(optionChapterGroupData)
        optionChapterGroupData.onClickItemListener =
            object : OptionChapterGroupData.OnClickItemListener {
                override fun onClickMenuItem(item: OptionChapter) {
                    when (item.type) {
                        TypeOption.PREVIOUS -> {
                            val prevIndex =
                                chapterSpinnerAdapter.index.previousIndex(chapterSpinnerAdapter.items.size)
                                    ?: return
                            spListChapter.clearSelectedItem()
                            chapterSpinnerAdapter.index = prevIndex
                            mPresenter.executeCommand(chapterSpinnerAdapter.items[prevIndex].id?.let {
                                ChangeChapterCmd(
                                    it
                                )
                            })
                            spListChapter.setSpinnerAdapter(chapterSpinnerAdapter)
                            spListChapter.selectItemByIndex(prevIndex)
                            spListChapter.dismiss()
                            tvChapterLabel.text = chapterSpinnerAdapter.items[prevIndex].title
                            mPresenter.executeCommand(
                                RefreshMenuCmd(
                                    optionChapterGroupData.menuChapter()!!,
                                    chapterSpinnerAdapter.items,
                                    chapterSpinnerAdapter.items[prevIndex].id!!
                                )
                            )
                            wvContent?.evaluateJavascript(
                                "window.scrollTo({ top: 0, behavior: 'smooth' });",
                                null
                            )
                            appBarLayout.setExpanded(true, true)
                            rcvOption.slideStart()
                        }

                        TypeOption.CHAPTER -> {
                            drawerLayout.openDrawer(GravityCompat.END)
                        }

                        TypeOption.SOUND -> {
                            mPresenter.executeCommand(ChangeStateSoundCmd())
                        }

                        TypeOption.COMMENT -> {
                            val currentIndex =
                                mListChapterNavGroupData.getItemStateArray().oldIndexOfChapter()
                                    ?: return
                            mPresenter.executeCommand(chapterSpinnerAdapter.items[currentIndex].id?.let {
                                MoveCommentChapterCmd(
                                    it
                                )
                            })
                        }

                        TypeOption.NEXT -> {
                            val nextIndex =
                                chapterSpinnerAdapter.index.nextIndex(chapterSpinnerAdapter.items.size)
                                    ?: return
                            mPresenter.executeCommand(
                                RefreshMenuCmd(
                                    optionChapterGroupData.menuChapter()!!,
                                    chapterSpinnerAdapter.items,
                                    chapterSpinnerAdapter.items[nextIndex].id!!
                                )
                            )
                            spListChapter.clearSelectedItem()
                            chapterSpinnerAdapter.index = nextIndex
                            mPresenter.executeCommand(chapterSpinnerAdapter.items[nextIndex].id?.let {
                                ChangeChapterCmd(
                                    it
                                )
                            })
                            spListChapter.setSpinnerAdapter(chapterSpinnerAdapter)
                            spListChapter.selectItemByIndex(nextIndex)
                            spListChapter.dismiss()
                            tvChapterLabel.text = chapterSpinnerAdapter.items[nextIndex].title
                            wvContent?.evaluateJavascript(
                                "window.scrollTo({ top: 0, behavior: 'smooth' });",
                                null
                            )
                            appBarLayout.setExpanded(true, true)
                            rcvOption.slideStart()
                        }
                    }
                }
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
        var currentPhonePos = if (chapterId == -1) {
            0
        } else {
            chapters.indexOfFirst { it.id == chapterId }
        }
        chapterSpinnerAdapter = ChapterSpinnerAdapter(
            context, chapters
        ) { oldIndex, oldItem, newIndex, newItem ->
            currentPhonePos = newIndex
            spListChapter.clearSelectedItem()
            chapterSpinnerAdapter.index = newIndex
            mPresenter.executeCommand(chapters[newIndex].id?.let { ChangeChapterCmd(it) })
            spListChapter.setSpinnerAdapter(chapterSpinnerAdapter)
            spListChapter.selectItemByIndex(currentPhonePos)
            spListChapter.dismiss()
            tvChapterLabel.text = chapters[newIndex].title
            fillDataToNavChapter(currentPhonePos, chapters)
            mPresenter.executeCommand(
                RefreshMenuCmd(
                    optionChapterGroupData.menuChapter()!!,
                    chapterSpinnerAdapter.items,
                    chapterSpinnerAdapter.items[newIndex].id!!
                )
            )
            wvContent?.evaluateJavascript(
                "window.scrollTo({ top: 0, behavior: 'smooth' });",
                null
            )
            appBarLayout.setExpanded(true, true)
            rcvOption.slideStart()
        }

        spListChapter.setOnClickListener {
            spListChapter.show()
            spListChapter.getSpinnerRecyclerView().layoutManager?.scrollToPosition(currentPhonePos)
        }

        if (chapterId == -1) {
            chapterSpinnerAdapter.index = 0
        } else {
            chapterSpinnerAdapter.index = chapters.indexOfFirst { it.id == chapterId }
        }

        spListChapter.setSpinnerAdapter(chapterSpinnerAdapter)
        spListChapter.selectItemByIndex(currentPhonePos)
        spListChapter.setIsFocusable(true)
        spListChapter.getSpinnerRecyclerView().layoutManager?.scrollToPosition(currentPhonePos)
        mPresenter.executeCommand(chapters[currentPhonePos].id?.let { ChangeChapterCmd(it) })
        tvChapterLabel.text = chapters[currentPhonePos].title

        fillDataToNavChapter(currentPhonePos, chapters)
    }

    private fun fillDataToNavChapter(currentPhonePos: Int, chapters: List<Chapter>) {
        mListChapterNavGroupData.getItemStateArray().forEach { key, _ ->
            mListChapterNavGroupData.getItemStateArray().put(key, false)
        }
        mListChapterNavGroupData.getItemStateArray().put(currentPhonePos, true)
        mListChapterNavGroupData.reset(chapters)
        mListChapterNavGroupData.show()
    }

    fun setStoryName(name: String) {
        tvChapterName?.text = name
    }

    fun loadChapterContent(storyUrl: String) {
        wvContent?.loadUrl(storyUrl)
    }

    fun fillDataToBottomMenu(listMenu: List<OptionChapter>) {
        optionChapterGroupData.reset(listMenu)
        optionChapterGroupData.show()
    }

    fun Int.nextIndex(listSize: Int): Int? {
        val nextIndex = this + 1
        return if (nextIndex < listSize) nextIndex else null
    }

    fun Int.previousIndex(listSize: Int): Int? {
        val previousIndex = this - 1
        return if (previousIndex >= 0) previousIndex else null
    }

    fun setStateSoundMenu(playing: Boolean) {
        optionChapterGroupData.menuChapter()?.get(3)!!.selected = playing
        optionChapterGroupData.reset(optionChapterGroupData.menuChapter())
        optionChapterGroupData.show()
    }

    class ChangeStateSoundCmd : ICommand
    class ChangeChapterCmd(val chapterId: Int) : ICommand
    class MoveCommentChapterCmd(val chapterId: Int) : ICommand
    class RefreshMenuCmd(
        val listMenu: List<OptionChapter>,
        val chapters: List<Chapter>,
        val chapterId: Int
    ) : ICommand

    class OnBackCmd : ICommand
}


