package com.quangph.jetpack.view.report

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quangph.base.R


/**
 * Report view like exel: a pinned header, a pinned left column, and content view which can vertically/horizontally scroll
 * The pinned left column is a recyclerview, and adapter is set by func: setFirstColumnAdapter(adapter)
 * The pinned header is a view which is set by func: setHeaderView(view)
 */
class TableView : LinearLayout, OnTableScrollChangeListener {

    var tvHeadIndex: TextView? = null
    var headViewStub: ViewStub? = null
    var headScrollView: TableScrollView? = null
    var contentScrollView: TableScrollView? = null
    var firstColumnRecyclerView: RecyclerView? = null
    var contentRecyclerView: RecyclerView? = null
    var onLoadMore: (() -> Unit)? = null
    var stillMore: Boolean = true
    var isLoadMore = false

    private var mLayoutManagerFirst: LinearLayoutManager? = null

    //    private var leftTopHeadLayoutResId = View.NO_ID
    private var headLayoutResId = View.NO_ID
    private var footerLayoutResId = View.NO_ID

    private var titleHeadColumnIndex: String? = null
    private var gravityHeadColumnIndex: Int = Gravity.CENTER
    private var colorHeadColumnIndex: Int? = null
    private var widthHeadColumnIndex: Int? = null
    private var heightHeadColumnIndex: Int? = null
    private var textSizeHeadColumnIndex: Int? = null
    private var backgroundColorHeadColumnIndex: Int? = null
    private var titleFooterColumnIndex: String? = null

    private var gravityFooterColumnIndex: Int = Gravity.CENTER
    private var colorFooterColumnIndex: Int? = null
    private var widthFooterColumnIndex: Int? = null
    private var heightFooterColumnIndex: Int? = null
    private var textSizeFooterColumnIndex: Int? = null
    private var backgroundColorFooterColumnIndex: Int? = null


    private var tvFooterIndex: TextView? = null
    private var footerViewStub: ViewStub? = null
    private var footerScrollView: TableScrollView? = null
    private var prbLoadMore: ProgressBar? = null

    private val CENTER = 0
    private val LEFT = 1
    private val RIGHT = 2
    private val CENTER_VERTICAL = 3
    private val CENTER_HORIZONTAL = 4

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
        orientation = VERTICAL
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
        orientation = VERTICAL
    }

    override fun onScrollChanged(scrollView: TableScrollView?, x: Int, y: Int) {
        if (scrollView == headScrollView) {
            contentScrollView!!.scrollTo(x, y)
        } else {
            headScrollView!!.scrollTo(x, y)
            footerScrollView!!.scrollTo(x, y)
        }
    }

    override fun onScrollFarLeft(scrollView: TableScrollView?) {
    }

    override fun onScrollFarRight(scrollView: TableScrollView?) {
    }

    fun setFirstColumnAdapter(firstColumnAdapter: RecyclerView.Adapter<*>?): TableView {
        firstColumnRecyclerView!!.adapter = firstColumnAdapter
        return this
    }

    fun setContentAdapter(contentAdapter: RecyclerView.Adapter<*>?): TableView {
        contentRecyclerView!!.adapter = contentAdapter
        return this
    }

    fun showLoadMore(isShow: Boolean) {
        if (isShow) {
            isLoadMore = true
            prbLoadMore?.visibility = View.VISIBLE
        } else {
            isLoadMore = false
            prbLoadMore?.visibility = View.GONE
        }
    }

    fun setHeaderView(view: View) {
        val container = findViewById<FrameLayout>(R.id.table_view_head_scroll_view)
        container.addView(view)
    }

    fun setFooterView(view: View) {
        val container = findViewById<FrameLayout>(R.id.table_view_footer_scroll_view)
        container.addView(view)
    }

    fun resetFooterView() {
        val container = findViewById<FrameLayout>(R.id.table_view_head_scroll_view)
        container.removeAllViews()
    }

    fun resetHeaderView() {
        val container = findViewById<FrameLayout>(R.id.table_view_footer_scroll_view)
        container.removeAllViews()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.TableView)
        headLayoutResId = typedArray.getResourceId(R.styleable.TableView_tblv_head_layout_res_id, View.NO_ID)
        footerLayoutResId = typedArray.getResourceId(R.styleable.TableView_tblv_footer_layout_res_id, View.NO_ID)

        when (typedArray.getInt(R.styleable.TableView_tblv_head_index_text_gravity, 0)) {
            CENTER -> gravityHeadColumnIndex = Gravity.CENTER
            LEFT -> gravityHeadColumnIndex = Gravity.START
            RIGHT -> gravityHeadColumnIndex = Gravity.END
            CENTER_HORIZONTAL -> gravityHeadColumnIndex = Gravity.CENTER_HORIZONTAL
            CENTER_VERTICAL -> gravityHeadColumnIndex = Gravity.CENTER_VERTICAL

        }
        titleHeadColumnIndex = typedArray.getString(R.styleable.TableView_tblv_head_index_text)
        colorHeadColumnIndex = typedArray.getColor(R.styleable.TableView_tblv_head_index_text_color, Color.TRANSPARENT)
        widthHeadColumnIndex = typedArray.getDimensionPixelSize(R.styleable.TableView_tblv_head_index_width, 0)
        heightHeadColumnIndex = typedArray.getDimensionPixelSize(R.styleable.TableView_tblv_head_index_height, 0)
        textSizeHeadColumnIndex = typedArray.getDimensionPixelSize(R.styleable.TableView_tblv_head_index_text_size, 0)
        backgroundColorHeadColumnIndex = typedArray.getColor(R.styleable.TableView_tblv_head_index_background_color, Color.TRANSPARENT)

        titleFooterColumnIndex = typedArray.getString(R.styleable.TableView_tblv_footer_index_text)
        colorFooterColumnIndex = typedArray.getColor(R.styleable.TableView_tblv_footer_index_text_color, Color.TRANSPARENT)
        widthFooterColumnIndex = typedArray.getDimensionPixelSize(R.styleable.TableView_tblv_footer_index_width, 0)
        heightFooterColumnIndex = typedArray.getDimensionPixelSize(R.styleable.TableView_tblv_footer_index_height, 0)
        textSizeFooterColumnIndex = typedArray.getDimensionPixelSize(R.styleable.TableView_tblv_footer_index_text_size, 0)
        backgroundColorFooterColumnIndex = typedArray.getColor(R.styleable.TableView_tblv_footer_index_background_color, Color.TRANSPARENT)

        when (typedArray.getInt(R.styleable.TableView_tblv_footer_index_text_gravity, 0)) {
            CENTER -> gravityFooterColumnIndex = Gravity.CENTER
            LEFT -> gravityFooterColumnIndex = Gravity.START
            RIGHT -> gravityFooterColumnIndex = Gravity.END
            CENTER_HORIZONTAL -> gravityFooterColumnIndex = Gravity.CENTER_HORIZONTAL
            CENTER_VERTICAL -> gravityFooterColumnIndex = Gravity.CENTER_VERTICAL
        }


        typedArray.recycle()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_table_view, this, true)
        prbLoadMore = findViewById(R.id.prbLoadMore)
        tvHeadIndex = findViewById(R.id.tvHeadIndex)
        tvFooterIndex = findViewById(R.id.tvFooterIndex)
        //headViewStub = findViewById(R.id.table_view_head_view_stub)
        //footerViewStub = findViewById(R.id.table_view_footer_view_stub)
        headScrollView = findViewById(R.id.table_view_head_scroll_view)
        footerScrollView = findViewById(R.id.table_view_footer_scroll_view)
        contentScrollView = findViewById(R.id.table_view_content_scroll_view)

        mLayoutManagerFirst = LinearLayoutManager(context)
        val mLayoutManagerContent = LinearLayoutManager(context)
        firstColumnRecyclerView = findViewById(R.id.table_view_first_column)
        firstColumnRecyclerView?.layoutManager = mLayoutManagerFirst
        contentRecyclerView = findViewById(R.id.table_view_content)
        contentRecyclerView?.layoutManager = mLayoutManagerContent
        firstColumnRecyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        contentRecyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        tvHeadIndex?.text = titleHeadColumnIndex
        tvHeadIndex?.setTextColor(colorHeadColumnIndex ?: Color.TRANSPARENT)
        tvHeadIndex?.setBackgroundColor(backgroundColorHeadColumnIndex ?: Color.TRANSPARENT)
        tvHeadIndex?.setTextSize(TypedValue.COMPLEX_UNIT_PX,(textSizeHeadColumnIndex?:0).toFloat())
        val paramHead = tvHeadIndex!!.layoutParams
        paramHead.width = widthHeadColumnIndex ?: 0
        paramHead.height = heightHeadColumnIndex ?: 0
        tvHeadIndex?.gravity = gravityHeadColumnIndex
        tvHeadIndex?.layoutParams = paramHead

        tvFooterIndex?.text = titleFooterColumnIndex
        tvFooterIndex?.setTextColor(colorFooterColumnIndex ?: Color.TRANSPARENT)
        tvFooterIndex?.setBackgroundColor(backgroundColorFooterColumnIndex ?: Color.TRANSPARENT)
        tvFooterIndex?.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSizeFooterColumnIndex?:0).toFloat())
        val paramFooter = tvFooterIndex!!.layoutParams
        paramFooter.width = widthFooterColumnIndex ?: 0
        paramFooter.height = heightFooterColumnIndex ?: 0
        tvFooterIndex?.gravity = gravityFooterColumnIndex
        tvFooterIndex?.layoutParams = paramFooter

        initViewStub()
        initViewListener()
    }

    private fun initViewStub() {
        if (headLayoutResId != View.NO_ID) {
           // headViewStub!!.layoutResource = headLayoutResId
            //headViewStub?.inflate()
        }
        if (footerLayoutResId != View.NO_ID) {
            /*footerViewStub!!.layoutResource = footerLayoutResId
            footerViewStub!!.inflate()*/
        }
    }

    private fun initViewListener() {
        headScrollView!!.setOnTableScrollChangeListener(this)
        contentScrollView!!.setOnTableScrollChangeListener(this)
        footerScrollView!!.setOnTableScrollChangeListener(this)
        firstColumnRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    contentRecyclerView!!.scrollBy(dx, dy)
                }
                if (dy > 0 && !isLoadMore && stillMore) {
                    val visibleItemCount = mLayoutManagerFirst!!.childCount
                    val totalItemCount = mLayoutManagerFirst!!.itemCount
                    val pastVisibleItems = mLayoutManagerFirst!!.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        isLoadMore = true
                        showLoadMore(true)
                        onLoadMore?.invoke()
                    }
                }
            }
        })
        contentRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    firstColumnRecyclerView!!.scrollBy(dx, dy)
                }
            }
        })
    }

}