package com.dede.dedegame.presentation.commom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.ViewPager2
import com.dede.dedegame.R

class IndicatorView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(attrs)
    }

    private var sliderView: View? = null
    private var selectedIcon: Drawable?
    private var unselectedIcon: Drawable?

    private var type = IndicatorType.NONE
    private var slideDuration = 250L
    private var iconWidth: Int
    private var iconHeight: Int
    private var iconSpace: Int
    private var dotCount = -1
    private var isInfiniteViewPager = false

    private var viewPager: ViewPager2? = null

    init {
        selectedIcon =
            AppCompatResources.getDrawable(context, R.drawable.shape_white500_bg_corner_2)
        unselectedIcon =
            AppCompatResources.getDrawable(context, R.drawable.shape_gray300_bg_corner_2)
        iconWidth = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._24sdp)
        iconHeight = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp)
        iconSpace = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._4sdp)
    }

    fun setUpWithViewPager2(viewPager2: ViewPager2?, isInfinite: Boolean = false) {
        this.isInfiniteViewPager = isInfinite
        viewPager = viewPager2
        viewPager?.let { vp ->
            registerAdapterDataObserver()
            populateDotsFromPagerAdapter()
            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectDotHighlight(if (isInfiniteViewPager) position - 1 else position)
                }
            })
        }
    }

    fun setIndicatorWidth(width: Int) {
        this.iconWidth = width
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.IndicatorView, 0, 0
            ).apply {
                try {
                    selectedIcon = getDrawable(R.styleable.IndicatorView_indicator_view_selected_icon)
                        ?: selectedIcon
                    unselectedIcon =
                        getDrawable(R.styleable.IndicatorView_indicator_view_unselected_icon)
                            ?: unselectedIcon
                    iconWidth = getDimensionPixelSize(
                        R.styleable.IndicatorView_indicator_view_icon_width, iconWidth
                    )
                    iconHeight = getDimensionPixelSize(
                        R.styleable.IndicatorView_indicator_view_icon_height, iconHeight
                    )
                    iconSpace = getDimensionPixelSize(
                        R.styleable.IndicatorView_indicator_view_space, iconSpace
                    )
                    slideDuration = getInteger(
                        R.styleable.IndicatorView_indicator_view_slide_duration, slideDuration.toInt()
                    ).toLong()
                    type = IndicatorType.enumOf(
                        getInteger(
                            R.styleable.IndicatorView_indicator_view_type, IndicatorType.NONE.value
                        )
                    )
                } finally {
                    recycle()
                }
            }
        }
    }

    private fun populateDotsFromPagerAdapter() {
        removeAllViews()
        dotCount = viewPager?.adapter?.itemCount ?: 0
        if (isInfiniteViewPager) {
            dotCount -= 2
        }
        if (dotCount > 1) {
            visibility = View.VISIBLE
            for (i in 0 until dotCount) {
                val indicatorView = createDotView(isTheFirstDot = i == 0)
                addView(indicatorView)
                val constrainSet = ConstraintSet()
                constrainSet.clone(this)
                constrainSet.connect(
                    indicatorView.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP
                )
                constrainSet.connect(
                    indicatorView.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM
                )

                if (i == 0) {
                    constrainSet.connect(
                        indicatorView.id, ConstraintSet.START, this.id, ConstraintSet.START
                    )
                } else {
                    constrainSet.connect(
                        indicatorView.id,
                        ConstraintSet.START,
                        this.getChildAt(i - 1).id,
                        ConstraintSet.END
                    )
                }
                constrainSet.applyTo(this)
            }
            if (type == IndicatorType.SLIDE) {
                addSliderView()
            }
            selectDotHighlight(
                position = if (isInfiniteViewPager) viewPager!!.currentItem - 1 else viewPager!!.currentItem,
                isSmoothScroll = false
            )
        } else {
            visibility = View.GONE
        }
    }

    private fun registerAdapterDataObserver() {
        viewPager?.adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                populateDotsFromPagerAdapter()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                populateDotsFromPagerAdapter()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                populateDotsFromPagerAdapter()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                populateDotsFromPagerAdapter()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                populateDotsFromPagerAdapter()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                populateDotsFromPagerAdapter()
            }
        })
    }

    private fun addSliderView() {
        sliderView = View(context).apply {
            id = View.generateViewId()
            background = selectedIcon
            layoutParams = LayoutParams(iconWidth, iconHeight)
        }
        addView(sliderView)
        val constrainSet = ConstraintSet()
        constrainSet.clone(this)

        sliderView?.let { iv ->
            constrainSet.connect(iv.id, ConstraintSet.START, this.id, ConstraintSet.START)
            constrainSet.connect(iv.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP)
            constrainSet.connect(iv.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM)
        }
        constrainSet.applyTo(this)
    }

    fun selectDotHighlight(position: Int, isSmoothScroll: Boolean = true) {
        if (position < dotCount) {
            when (type) {
                IndicatorType.NONE -> selectDot(position)
                IndicatorType.SLIDE -> slideDot(position, isSmoothScroll)
            }
        }
    }

    private fun selectDot(position: Int) {
        for (i in 0 until this.childCount) {
            val ivDot = getChildAt(i) as ImageView
            if (i == position) {
                ivDot.setImageDrawable(selectedIcon)
            } else {
                ivDot.setImageDrawable(unselectedIcon)
            }
        }
    }

    private fun slideDot(position: Int, isSmoothScroll: Boolean = true) {
        val slider = sliderView ?: return
        val pos = if (position >= 0) position else 0
        this.getChildAt(pos)?.post {
            slider.animate().translationX(this.getChildAt(pos).x)
                .setDuration(if (isSmoothScroll) slideDuration else 0L)
                .start()
        }
    }

    private fun createDotView(isTheFirstDot: Boolean): View {
        return View(context).apply {
            id = View.generateViewId()
            background = unselectedIcon
            layoutParams = LayoutParams(iconWidth, iconHeight).apply {
                if (!isTheFirstDot) {
                    marginStart = iconSpace
                }
            }
        }
    }


    enum class IndicatorType(val value: Int) {
        NONE(0), SLIDE(1);

        companion object {
            fun enumOf(value: Int) = when (value) {
                1 -> SLIDE
                else -> NONE
            }
        }
    }
}