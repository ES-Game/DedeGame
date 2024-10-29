package com.dede.dedegame.presentation.story_search

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.common.GridSpacingItemDecoration
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListViewType
import com.dede.dedegame.presentation.widget.EndlessRecyclerViewScrollListener
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class StorySearchView(context: Context?, attrs: AttributeSet?) :
    BaseConstraintView(context, attrs) {

    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val imvClose by lazy { findViewById<ImageView>(R.id.imvEnd) }
    private val edtSearch by lazy { findViewById<TextView>(R.id.edtSearch) }
    private val emptyView by lazy { findViewById<TextView>(R.id.tvEmptyTitle) }
    private var storyListAdapter = StoryListAdapter()
    private val layoutManager = GridLayoutManager(context, 3)
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var mCurrentPage = 0
    private var mLastPage = 1
    private var isLoading = false
    private var currentKeyword = ""

    fun getCurrentKeyword(): String {
        return currentKeyword
    }

    fun setCurrentKeyword(keyword: String) {
        currentKeyword = keyword
    }

    fun setCurrentPage(currentPage: Int) {
        mCurrentPage = currentPage
    }

    fun currentPage(): Int {
        return mCurrentPage
    }


    fun setLastPage(lastPage: Int) {
        mLastPage = lastPage
    }

    fun lastPage(): Int {
        return mLastPage
    }

    fun setLoading(loading: Boolean) {
        isLoading = loading
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    override fun onInitView() {
        super.onInitView()
        setupToolbar()

        rcvInfo.adapter = storyListAdapter
        storyListAdapter.setOnClickListener(object : StoryListAdapter.OnClickListener {
            override fun onClickGameItem(item: StoryDetail) {
                mPresenter.executeCommand(GotoStoryCoverCmd(item))
            }
        })
        rcvInfo.layoutManager = layoutManager
        rcvInfo.addItemDecoration(GridSpacingItemDecoration(3, DimensUtil.dpToPx(16), true))
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (mCurrentPage <= mLastPage && !isLoading) {
                    mPresenter.executeCommand(LoadMoreCmd())
                }
            }
        }
        rcvInfo.addOnScrollListener(scrollListener)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = storyListAdapter.getItemViewType(position)
                return if (itemViewType == StoryListViewType.ITEM_LOAD_MORE) {
                    3
                } else {
                    1
                }
            }

        }

        edtSearch.requestFocus()
        edtSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edtSearch.text.toString().isNotEmpty()) {
                        mPresenter.executeCommand(SearchStoryCmd(edtSearch.text.toString()))
                        clearFocusEditText()
                    } else {
                        Toast.makeText(context, "Please enter keyword", Toast.LENGTH_SHORT).show()
                    }
                    return true
                }
                return false
            }
        })

        imvClose.setOnClickListener {
            edtSearch.text = ""
            currentKeyword = ""
        }
        showEmptyView(false)
    }

    private fun setupToolbar() {
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }


    fun fillStoryToAdapter(data: List<StoryDetail>) {
        storyListAdapter.setListStory(data)
    }

    fun loadMore(data: List<StoryDetail>) {
        storyListAdapter.addItems(data)
    }

    fun addLoadingFooter() {
        storyListAdapter.addLoadingFooter()
    }

    fun removeLoadingFooter() {
        storyListAdapter.removeLoadingFooter()
    }

    fun clearFocusEditText() {
        edtSearch.clearFocus()
        hideKeyboard()
    }

    fun resetListView(){
        storyListAdapter.setListStory(emptyList())
        scrollListener.resetState()
    }

    fun showEmptyView(shown : Boolean){
        if (shown){
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.INVISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edtSearch?.windowToken, 0)
    }

    class OnBackCmd : ICommand
    class GotoStoryCoverCmd(val storyDetail: StoryDetail) : ICommand
    class SearchStoryCmd(val keyword: String) : ICommand
    class LoadMoreCmd : ICommand

}


