package com.quangph.jetpack.view.report.cell

import android.view.View
import com.quangph.jetpack.view.report.cell.CellConfig
import com.quangph.jetpack.view.report.cell.CellInfo

/**
 * Created by QuangPH on 2020-08-14.
 */
interface IRowRender {
    fun config(cellConfigList: List<CellConfig>)
    fun getCellConfig(): List<CellConfig>
    fun renderColumn(cellList: List<CellInfo>): View
    fun getCellInfoOfRow(rowView: View): List<CellInfo>
    fun refreshCellInfo(rowView: View)
}