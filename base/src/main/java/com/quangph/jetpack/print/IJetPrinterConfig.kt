package com.quangph.jetpack.print

/**
 * Created by QuangPH on 2020-08-28.
 */
interface IJetPrinterConfig {
    fun getAddress(): String?
    fun getType(): JET_PRINTER_TYPE
    fun getFormSize(): JET_PRINT_SIZE
}