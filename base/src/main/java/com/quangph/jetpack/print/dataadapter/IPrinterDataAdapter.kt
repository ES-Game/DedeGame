package com.quangph.jetpack.print.dataadapter

/**
 * This interface will convert data to printed data
 * Created by QuangPH on 2020-08-26.
 */
interface IPrinterDataAdapter<S, D> {
    fun generateData(source: S): D
}