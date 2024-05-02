package com.quangph.jetpack.view.report.cell

import android.view.Gravity

/**
 * Info for build a cell view which is a child of CellGroupLayout
 * Created by QuangPH on 2020-08-14.
 */
class CellInfo {
    var startRowIndex: Int = -1
    var endRowIndex: Int = startRowIndex
    var startColIndex: Int = -1
    var endColIndex: Int = startColIndex
    var title: String? = null
    var textSize: Float? = null
    var textGravity: Int = Gravity.CENTER
    var fontRes: Int? = null
    var textColor: Int = android.R.color.black
    var showTopLine = false
    var showBottomLine = false
    var showLeftLine = false
    var showRightLine = false
    var bgColor: Int = android.R.color.white
    var onClicked: ((CellInfo) -> Unit)? = null
}