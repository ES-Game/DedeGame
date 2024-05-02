package com.quangph.jetpack.view.tooltip

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.quangph.base.R

class TooltipContainerView2(context: Context?) : RelativeLayout(context) {

    var anchorView: View? = null
    var bgColor: Int = Color.BLACK
    var cornerRadius = 0f

    private var vArrowAbove: View? = null
    private var vArrowBelow: View? = null
    private var flContainer: FrameLayout? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_tooltip_container, this, true)
        vArrowAbove = findViewById(R.id.arrow_above)
        vArrowBelow = findViewById(R.id.arrow_below)
        flContainer = findViewById(R.id.container)
        vArrowAbove?.background = ArrowDrawable(ArrowDrawable.TOP, bgColor)
        vArrowBelow?.background = ArrowDrawable(ArrowDrawable.BOTTOM, bgColor)
        vArrowAbove?.isClickable = true
        vArrowBelow?.isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (anchorView == null) return
        val anchorLoc = getLocationPositionOnScreen(anchorView!!)
        if (hasBelowSpace(anchorLoc[1])) {
            showBelow(anchorLoc)
        } else if (hasAboveSpace(anchorLoc[1])) {
            showAbove(anchorLoc)
        } else {
            if (forceShowAbove(anchorLoc[1])) {
                showAbove(anchorLoc)
            } else {
                showBelow(anchorLoc)
            }
        }
    }

    fun setContentView(layout: Int) {
        val v = LayoutInflater.from(context).inflate(layout, flContainer, false)
        v.isClickable = true
        v.background = generateContentBackground()
        flContainer?.addView(v)

        vArrowAbove?.background = ArrowDrawable(ArrowDrawable.TOP, bgColor)
        vArrowBelow?.background = ArrowDrawable(ArrowDrawable.BOTTOM, bgColor)
    }

    private fun hasBelowSpace(anchorY: Int): Boolean {
        val belowSpace = measuredHeight - anchorY - (anchorView?.height ?: 0)
        return belowSpace >= (flContainer!!.measuredHeight + vArrowAbove!!.measuredHeight)
    }

    private fun showBelow(anchoLoc: IntArray) {
        vArrowBelow?.visibility = GONE
        vArrowAbove?.x = getAnchorCenterX(anchoLoc[0].toFloat()) - vArrowAbove!!.measuredWidth / 2f
        vArrowAbove?.y = (anchoLoc[1] + anchorView!!.height).toFloat()
        flContainer?.y = vArrowAbove!!.y + vArrowAbove!!.measuredHeight
        setContentX(getAnchorCenterX(anchoLoc[0].toFloat()))
    }

    private fun hasAboveSpace(anchorY: Int): Boolean {
        val aboveSpace = anchorY
        return aboveSpace >= (flContainer!!.measuredHeight + vArrowBelow!!.measuredHeight)
    }

    private fun showAbove(anchorLoc: IntArray) {
        vArrowAbove?.visibility = GONE
        vArrowBelow?.x = getAnchorCenterX(anchorLoc[0].toFloat()) - vArrowBelow!!.measuredWidth / 2f
        vArrowBelow?.y = anchorLoc[1].toFloat() - vArrowBelow!!.measuredHeight
        flContainer?.y = vArrowBelow!!.y - flContainer!!.measuredHeight
        setContentX(getAnchorCenterX(anchorLoc[0].toFloat()))
    }

    private fun setContentX(anchorCenterX: Float) {
        val startX = anchorCenterX - flContainer!!.measuredWidth / 2
        val endX = anchorCenterX + flContainer!!.measuredWidth / 2
        if ((startX >= 0f) && (endX < measuredWidth)) {
            flContainer?.x = startX
        } else {
            if (startX < 0f) {
                flContainer?.x = 0f
            } else {
                flContainer?.x = (measuredWidth - flContainer!!.measuredWidth).toFloat()
            }
        }
    }

    private fun getAnchorCenterX(anchorX: Float): Float {
        return anchorX + anchorView!!.width / 2f
    }

    private fun forceShowAbove(anchorY: Int): Boolean {
        val belowSpace = measuredHeight - anchorY - (anchorView?.height ?: 0)
        val aboveSpace = anchorY
        return aboveSpace > belowSpace
    }

    private fun generateContentBackground(): Drawable {
        val bg = GradientDrawable()
        val rad = pxFromDp(context, cornerRadius)
        val radius = floatArrayOf(rad, rad,
            rad, rad,
            rad, rad,
            rad, rad)
        bg.cornerRadii = radius
        bg.color = ColorStateList.valueOf(bgColor)
        return bg
    }

    private fun getLocationPositionOnScreen(v: View): IntArray {
        val loc = IntArray(2)
        v.getLocationOnScreen(loc)
        loc[1] -= getStatusBarHeight()
        return loc
    }

    private fun getStatusBarHeight(): Int {
        return getInternalDimensionSize(context.resources, "status_bar_height")
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId: Int = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}