package com.quangph.jetpack.view.report

interface OnTableScrollChangeListener {
    fun onScrollChanged(scrollView: TableScrollView?, x: Int, y: Int)
    fun onScrollFarLeft(scrollView: TableScrollView?)
    fun onScrollFarRight(scrollView: TableScrollView?)
}