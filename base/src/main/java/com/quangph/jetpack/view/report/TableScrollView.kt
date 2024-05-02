package com.quangph.jetpack.view.report

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

class TableScrollView : HorizontalScrollView {
    private var downX = 0f
    private var upX = 0f
    private var onScrollChangeListener: OnTableScrollChangeListener? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> downX = ev.x
            MotionEvent.ACTION_UP -> {
                upX = ev.x
                if (computeHorizontalScrollOffset() == 0 && downX - upX < 0) {
                    if (onScrollChangeListener != null) {
                        onScrollChangeListener!!.onScrollFarLeft(this)
                    }
                } else if (computeHorizontalScrollRange() - computeHorizontalScrollOffset()
                        <= computeHorizontalScrollExtent() && downX - upX > 0) {
                    if (onScrollChangeListener != null) {
                        onScrollChangeListener!!.onScrollFarRight(this)
                    }
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (onScrollChangeListener != null) {
            onScrollChangeListener!!.onScrollChanged(this, l, t)
        }
    }

    fun setOnTableScrollChangeListener(onScrollChangeListener: OnTableScrollChangeListener?) {
        this.onScrollChangeListener = onScrollChangeListener
    }
}