package com.quangph.jetpack.view.report.cell

/**
 * Config info for a cell
 * Created by QuangPH on 2020-08-14.
 */
class CellConfig {
    var colIndex: Int = -1
    var rowIndex: Int = -1

    /**
     * Dimension of cell. If these dimens is not set, it will be automatically calculated by title
     * size and the max title size: maxValueStr
     */
    var width: Int? = null
    var height: Int = 0

    var title: String = ""
    var maxValueStr: String = ""
    var textSize: Float = 0f
}