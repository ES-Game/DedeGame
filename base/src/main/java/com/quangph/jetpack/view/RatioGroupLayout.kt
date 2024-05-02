package com.quangph.jetpack.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.quangph.base.R

/**
 * Created by QuangPH on 2021-01-18.
 */
open class RatioGroupLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private var spacing = 0f

    init {
        init(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        when {
            h == 0 -> {
                autoHeightStretch()
            }
            w == 0 -> {
                autoWidthStretch()
            }
            else -> {
                measureIntrinsic(w, h)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val size = calculateMatrixSize()
        val rowCount = size[0]
        val colCount = size[1]
        val widthUnit = (r - l - paddingLeft - paddingRight).toFloat() / (colCount - 1).toFloat()
        val heightUnit: Float = (b - t - paddingTop - paddingBottom).toFloat() / (rowCount - 1).toFloat()
        val spaceWidthRatio = (colCount - 1 - 1).toFloat() / (colCount - 1).toFloat()
        val spaceHeightRatio = (rowCount - 1 - 1).toFloat() / (rowCount - 1).toFloat()

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as LayoutParams
            var leftCol = params.leftColIndex * widthUnit + paddingLeft
            var rightCol = params.rightColIndex * widthUnit + paddingLeft
            var topRow = params.topRowIndex * heightUnit + paddingTop
            var bottomRow = params.bottomRowIndex * heightUnit + paddingTop

            if (params.topRowIndex > 0 || params.bottomRowIndex <= size[0] - 1) {
                //child.layout(leftCol.toInt(), topRow.toInt(), rightCol.toInt(), bottomRow.toInt())
                val lastHRatio = ((rowCount - 1 - 1 - (params.bottomRowIndex - 1 - 1)).toFloat()) / (rowCount - 1)
                val topHRatio = 1f - lastHRatio

                if (params.topRowIndex != 0) {
                    topRow += topHRatio * spacing
                }

                if (params.bottomRowIndex != size[0] - 1) {
                    val bottomHRatio = spaceHeightRatio - topHRatio
                    bottomRow -= bottomHRatio * spacing
                }
            }

            if (params.leftColIndex == 0 && params.rightColIndex == size[1] - 1) {
                child.layout(leftCol.toInt(), topRow.toInt(), rightCol.toInt(), bottomRow.toInt())
            } else {
                val lastWRatio = ((colCount - 1 - 1 - (params.rightColIndex - 1 - 1)).toFloat()) / (colCount - 1)
                val leftWRatio = 1f - lastWRatio

                if (params.leftColIndex != 0) {
                    leftCol += leftWRatio * spacing
                }

                if (params.rightColIndex != size[1] - 1) {
                    val rightWRatio = spaceWidthRatio - leftWRatio
                    rightCol -= rightWRatio * spacing
                }

                child.layout(leftCol.toInt(), topRow.toInt(), rightCol.toInt(), bottomRow.toInt())
            }
        }
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams? {
        return LayoutParams()
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams? {
        return RatioGroupLayout.LayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams? {
        return LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is RatioGroupLayout.LayoutParams
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatioGroupLayout)
        spacing = a.getDimension(R.styleable.RatioGroupLayout_ratio_group_layout2_spacing, 0f)
        a.recycle()
    }

    private fun autoHeightStretch(){}

    private fun autoWidthStretch(){}

    private fun measureIntrinsic(w: Int, h: Int) {
        val size = calculateMatrixSize()
        val rowCount = size[0]
        val colCount = size[1]
        val cellW = (w - ((colCount - 1) - 1) * spacing - paddingLeft - paddingRight) / (colCount - 1).toFloat()
        val cellH = (h - ((rowCount - 1) - 1) * spacing - paddingTop - paddingBottom) / (rowCount - 1).toFloat()

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as LayoutParams
            val spacingWCount = params.rightColIndex - params.leftColIndex - 1
            val childW = (spacingWCount + 1) * cellW + spacingWCount * spacing

            val spacingHCount = params.bottomRowIndex - params.topRowIndex - 1
            val childH = (spacingHCount + 1) * cellH + spacingHCount * spacing

            val childWSpec = MeasureSpec.makeMeasureSpec(childW.toInt(), MeasureSpec.EXACTLY)
            val childHSpec = MeasureSpec.makeMeasureSpec(childH.toInt(), MeasureSpec.EXACTLY)
            child.measure(childWSpec, childHSpec)
        }
    }

    private fun calculateMatrixSize(): IntArray {
        val size = IntArray(2)
        var tempCol = 0
        var tempRow = 0
        for (i in 0 until childCount) {
            val params = getChildAt(i).layoutParams as LayoutParams
            if (tempCol < params.rightColIndex) {
                tempCol = params.rightColIndex
            }

            if (tempRow < params.bottomRowIndex) {
                tempRow = params.bottomRowIndex
            }
        }

        size[0] = tempRow + 1
        size[1] = tempCol + 1
        return size
    }


    class LayoutParams : MarginLayoutParams {
        var leftColIndex = 0
        var topRowIndex = 0
        var rightColIndex = 0
        var bottomRowIndex = 0

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            val a = c.obtainStyledAttributes(attrs, R.styleable.RatioGroupLayout_Layout)
            leftColIndex = a.getInt(R.styleable.RatioGroupLayout_Layout_ratio_group_layout2_left_col_index, 0)
            topRowIndex = a.getInt(R.styleable.RatioGroupLayout_Layout_ratio_group_layout2_top_row_index, 0)
            rightColIndex = a.getInt(R.styleable.RatioGroupLayout_Layout_ratio_group_layout2_right_col_index, 1)
            bottomRowIndex = a.getInt(R.styleable.RatioGroupLayout_Layout_ratio_group_layout2_bottom_row_index, 1)
            a.recycle()
        }

        constructor() : super(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        constructor(params: ViewGroup.LayoutParams?) : super(params) {}
        constructor(src: MarginLayoutParams?) : super(src) {}
        constructor(width: Int, height: Int) : super(width, height) {}
    }
}