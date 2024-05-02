package com.quangph.jetpack.view.report.cell

import android.content.Context
import android.view.ViewGroup

/**
 * Row view like a row in excel. It contains multi cells
 * Created by QuangPH on 2020-08-15.
 */
class CellGroupLayout(context: Context?) : ViewGroup(context) {

    lateinit var cellConfigList: List<CellConfig>

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var w = 0
        cellConfigList.forEach {
            w += it.width ?: 0
        }

        var maxRowIndex = 0
        for (i in 0 until childCount) {
            val params = getChildAt(i).layoutParams as LayoutParams
            if (maxRowIndex < params.cellInfo?.endRowIndex ?: 0) {
                maxRowIndex = params.cellInfo?.endRowIndex ?: 0
            }
        }
        val height = cellConfigList[0].height * (maxRowIndex + 1)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as LayoutParams
            child.measure(MeasureSpec.makeMeasureSpec(calculateWidth(params.cellInfo!!), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(calculateHeight(params.cellInfo!!), MeasureSpec.EXACTLY))
        }

        setMeasuredDimension(w, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams as LayoutParams
            if (layoutParams.cellInfo != null) {
                val childLeft = calculateChildLeft(layoutParams.cellInfo!!)
                val childRight = childLeft + calculateWidth(layoutParams.cellInfo!!)
                val childTop = calculateChildTop(layoutParams.cellInfo!!)
                val childBottom = childTop + calculateHeight(layoutParams.cellInfo!!)
                child.layout(childLeft, childTop, childRight, childBottom)
            }
        }
    }

    private fun calculateChildLeft(cellInfo: CellInfo): Int {
        if (cellInfo.startColIndex == 0) return 0
        var prevColIndex = cellInfo.startColIndex - 1
        var left = 0
        for (i in 0..prevColIndex) {
            left += findCellConfigByCol(i).width ?: 0
        }
        return left
    }

    private fun calculateChildTop(cellInfo: CellInfo): Int {
        return cellInfo.startRowIndex * cellConfigList[0].height
    }

    private fun calculateWidth(cellInfo: CellInfo) : Int {
        var w = 0
        for (i in cellInfo.startColIndex..cellInfo.endColIndex) {
            w += findCellConfigByCol(i).width ?: 0
        }
        return w
    }

    private fun calculateHeight(cellInfo: CellInfo): Int {
        return (cellInfo.endRowIndex - cellInfo.startRowIndex + 1) * cellConfigList[0].height
    }

    private fun findCellConfigByCol(colIndex: Int): CellConfig {
        for (i in 0..cellConfigList.size) {
            if (cellConfigList[i].colIndex == colIndex) {
                return cellConfigList[i]
            }
        }

        throw IllegalArgumentException("Can not find config for column index: $colIndex")
    }


    class LayoutParams(width: Int, height: Int) : MarginLayoutParams(width, height) {
        var cellInfo: CellInfo? = null
    }
}