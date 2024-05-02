package com.quangph.jetpack.view.report.cell

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quangph.base.view.recyclerview.kt.KRclvVHInfo

/**
 * VH info for RecyclerView.Adapter used in content. The params rowRender is used for render a row of table
 * Created by QuangPH on 2020-08-15.
 */
abstract class CellRclvVHInfo<T>(private val rowRender: IRowRender): KRclvVHInfo<T>() {

    override fun createItemView(layoutInflater: LayoutInflater, viewGroup: ViewGroup): View {
        return rendRow(rowRender)
    }

    override fun onBind(itemView: View, data: T) {
        val cellInfoList = rowRender.getCellInfoOfRow(itemView)
        onBindCellInfo(cellInfoList, data)
        rowRender.refreshCellInfo(itemView)
    }

    abstract fun rendRow(rowRender: IRowRender): View
    abstract fun onBindCellInfo(cellInfoList: List<CellInfo>, data: T)
}