package com.quangph.jetpack.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.quangph.base.R
import com.quangph.base.view.drag.MoveGestureDetector

/**
 * Swipe item in recyclerview
 * Created by QuangPH on 2020-08-20.
 */
class SwipeItemView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    interface IOnBoundListener {
        fun onBoundReach(isLeft: Boolean)
    }

    var enableSwipe: Boolean = true
    var boundReachListener: IOnBoundListener? = null

    private var swipeItemId: Int = -1
    private var swipeView: View? = null
    private lateinit var moveGestureDetector: MoveGestureDetector

    init {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.SwipeItemView, 0, 0)
        swipeItemId = ta.getResourceId(R.styleable.SwipeItemView_swipe_item_swipe_view_id, -1)
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        swipeView = findViewById(swipeItemId)
        moveGestureDetector = MoveGestureDetector(swipeView,
            object : MoveGestureDetector.MoveGestureListener() {
                override fun getLeftBound(currentViewX: Float, currEvent: MotionEvent?): Float {
                    return this@SwipeItemView.width.toFloat() * (-1)
                }

                override fun getRightBound(currentViewX: Float, currEvent: MotionEvent?): Float {
                    return 0f
                }

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return swipeView?.performClick() ?: super.onSingleTapUp(e)
                }

            })

        moveGestureDetector.setGestureListener(object : MoveGestureDetector.IOnGestureListener {
            override fun onChangeState(prevState: Int, currentState: Int) {

            }

            override fun onMove(leftX: Float) {

            }

            override fun onEnd(isLeft: Boolean) {
                Log.e("Swipe", "onEnd: $isLeft")
                boundReachListener?.onBoundReach(isLeft)
            }
        })

        if (enableSwipe) {
            swipeView?.setOnTouchListener { _, event ->
                moveGestureDetector!!.onTouchEvent(event)
            }
        }
    }

    fun scrollToRight() {
        moveGestureDetector.scrollXToRight()
    }

    fun enableSwipe(enable: Boolean) {
        enableSwipe = enable
        if (enable) {
            swipeView?.setOnTouchListener { _, event ->
                moveGestureDetector?.onTouchEvent(event) ?: false
            }

        } else {
            swipeView?.setOnTouchListener { _, _ ->
                false
            }
        }
    }
}