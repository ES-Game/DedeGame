package com.quangph.jetpack.view.report.cell

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.*
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.quangph.base.R
import kotlin.math.max

/**
 * Created by QuangPH on 2020-08-14.
 */
open class RowRenderImpl(private val context: Context, private val fontRes: Int? = null): IRowRender {

    private lateinit var cellConfigList: List<CellConfig>
    private val dividerSize = context.resources.getDimensionPixelSize(R.dimen.dimen_1)
    private val paddingHorizontal = context.resources.getDimension(R.dimen.dimen_12)

    override fun config(cellConfigList: List<CellConfig>) {
        this.cellConfigList = cellConfigList
        extractCellInfo(cellConfigList)
    }

    override fun getCellConfig(): List<CellConfig> {
        return cellConfigList
    }


    override fun renderColumn(cellList: List<CellInfo>): View {
        val containerView = CellGroupLayout(context)
        containerView.cellConfigList = cellConfigList
        cellList.forEach {
            val cellView = createCellTextView(it)
            containerView.addView(cellView)
        }
        containerView.tag = cellList
        return containerView
    }

    override fun getCellInfoOfRow(rowView: View): List<CellInfo> {
        return (rowView as CellGroupLayout).tag as List<CellInfo>
    }

    override fun refreshCellInfo(rowView: View) {
        val cellContainer = rowView as CellGroupLayout
        for (i in 0 until cellContainer.childCount) {
            /*val child = cellContainer.getChildAt(i) as FrameLayout
            val cellInfo = child.tag as CellInfo
            val textView = child.getChildAt(0) as TextView
            textView.text = cellInfo.title*/

            val child = cellContainer.getChildAt(i) as TextView
            val params = child.layoutParams as CellGroupLayout.LayoutParams
            child.text = params.cellInfo?.title
            child.setTextColor(ContextCompat.getColor(context,params.cellInfo?.textColor?: android.R.color.black))
        }
    }

    private fun extractCellInfo(cellConfigList: List<CellConfig>) {
        val maxCellHeight = getMaxCellHeight(cellConfigList)
        val paint = Paint()
        if (fontRes != null) {
            paint.typeface = ResourcesCompat.getFont(context, fontRes)
        }

        paint.textSize = context.resources.getDimension(R.dimen.text_size_12)

        cellConfigList.forEach {
            it.height = maxCellHeight

            if (it.width == null) {
                it.width = getMaxWidth(it.title, it.maxValueStr, paint)
            }
        }
    }

    private fun createCellTextView(cellInfo: CellInfo): View {
        var width = 0
        for (i in cellInfo.startColIndex..cellInfo.endColIndex) {
            width += getCellConfigForCol(i)?.width ?: 0
        }

        var height = 0
        for (i in cellInfo.startRowIndex..cellInfo.endRowIndex) {
            height += cellConfigList[0].height
        }

        val textView = TextView(context)
        val param = CellGroupLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height)
        param.cellInfo = cellInfo
        textView.layoutParams = param

        textView.gravity = cellInfo.textGravity
        textView.isSingleLine = true
        textView.maxLines = 1
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.setPadding((paddingHorizontal / 2).toInt(), 0, (paddingHorizontal / 2).toInt(), 0)
        textView.setTextColor(ContextCompat.getColor(context, cellInfo.textColor))
        if (cellInfo.textSize == null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_12))
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, cellInfo.textSize!!)
        }

        cellInfo.fontRes?.let {
            textView.typeface = ResourcesCompat.getFont(context, it)
        }

        textView.text = cellInfo.title

        textView.background = createCellDrawable(cellInfo.bgColor, cellInfo.showLeftLine,
                cellInfo.showTopLine,
                cellInfo.showRightLine,
                cellInfo.showBottomLine)
        cellInfo.onClicked?.let { onClicked ->
            textView.setOnClickListener {
                onClicked.invoke(cellInfo)
            }
        }
        return textView
    }

    private fun getMaxCellHeight(cellConfigList: List<CellConfig>): Int {
        var max = 0
        cellConfigList.forEach {
            if (max < it.height) {
                max = it.height
            }
        }
        return max
    }

    private fun getMaxWidth(title: String, maxStr: String, paint: Paint): Int {
        val bound = Rect()
        paint.getTextBounds(title, 0, title.length, bound)
        val w = bound.width()

        bound.set(0, 0, 0, 0)
        paint.getTextBounds(maxStr, 0, maxStr.length, bound)
        val maxW = bound.width() + paddingHorizontal.toInt()

        return max(w, maxW)
    }

    private fun getCellConfigForCol(colIndex: Int): CellConfig? {
        for (i in cellConfigList.indices) {
            if (colIndex == cellConfigList[i].colIndex) {
                return cellConfigList[i]
            }
        }

        return null
    }


    private fun createCellDrawable(bgColor: Int, leftBound: Boolean, topBound: Boolean, rightBound: Boolean, bottomBound: Boolean): Drawable {
        val boundDrawable = GradientDrawable()
        boundDrawable.shape = GradientDrawable.RECTANGLE
        boundDrawable.setColor(Color.TRANSPARENT)
        boundDrawable.setStroke(dividerSize, Color.parseColor("#E4E4E4"))

        val bgDrawable = GradientDrawable()
        bgDrawable.shape = GradientDrawable.RECTANGLE
        bgDrawable.setColor(ContextCompat.getColor(context, bgColor))

        var left = 0
        if (leftBound) {
            left = dividerSize
        }

        var top = 0
        if (topBound) {
            top = dividerSize
        }

        var right = 0
        if (rightBound) {
            right = dividerSize
        }

        var bottom = 0
        if (bottomBound) {
            bottom = dividerSize
        }

        val layer = LayerDrawable(arrayOf(boundDrawable, bgDrawable))
        layer.setLayerInset(1, left, top, right, bottom)
        return layer
    }
}