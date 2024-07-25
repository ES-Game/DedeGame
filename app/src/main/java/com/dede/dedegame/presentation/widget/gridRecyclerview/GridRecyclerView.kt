package com.dede.dedegame.presentation.widget.gridRecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R

class GridRecyclerView : RecyclerView {
    private var manager: GridLayoutManager? = null
    private var columnNum = 1
    private var maxRow = 1
    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var itemMargin = 0
    private var selectedPosition = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attrsArray = intArrayOf(R.attr.columnNum)
            var array = context.obtainStyledAttributes(attrs, attrsArray)
            columnNum = array.getInt(0, 1)
            attrsArray[0] = R.attr.maxRow
            array = context.obtainStyledAttributes(attrs, attrsArray)
            maxRow = array.getInt(0, 1)
            attrsArray[0] = R.attr.itemMargin
            array = context.obtainStyledAttributes(attrs, attrsArray)
            itemMargin = array.getDimensionPixelSize(0, 0)
            array.recycle()
            onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                manager = SquareGridLayoutManager(getContext(), columnNum, itemMargin)
                layoutManager = manager
                manager?.scrollToPositionWithOffset(selectedPosition, 0)
                addItemDecoration(
                    AutoResizeItemDecoration(
                        width,
                        height,
                        columnNum,
                        itemMargin,
                        maxRow
                    )
                )
                viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
            }
            viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    fun scrollToPos(position: Int) {
        this.selectedPosition = position
    }

    fun setColumn(column: Int) {
        this.columnNum = column
        manager = SquareGridLayoutManager(context, columnNum, itemMargin)
        layoutManager = manager
        addItemDecoration(AutoResizeItemDecoration(width, height, columnNum, itemMargin, maxRow))
    }

    fun setMaxRow(maxRow: Int) {
        this.maxRow = maxRow
        manager = SquareGridLayoutManager(context, columnNum, itemMargin)
        layoutManager = manager
        addItemDecoration(AutoResizeItemDecoration(width, height, columnNum, itemMargin, maxRow))
    }

    fun setItemMargin(margin: Int) {
        this.itemMargin = margin
        manager = SquareGridLayoutManager(context, columnNum, margin)
        layoutManager = manager
        addItemDecoration(AutoResizeItemDecoration(width, height, columnNum, margin, maxRow))
    }
}