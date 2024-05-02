package com.quangph.jetpack.print

import android.bluetooth.BluetoothAdapter
import android.graphics.Bitmap
import com.quangph.jetpack.print.dataadapter.esc.ESC_POS_Command
import com.quangph.jetpack.print.dataadapter.esc.ESC_POS_PrinterDataAdapter
import com.quangph.jetpack.print.dataadapter.tsc.TSC_PrinterDataAdapter
import com.quangph.jetpack.print.core.bluetooth.BluetoothPrinter
import com.quangph.jetpack.print.core.IPrinter
import com.quangph.jetpack.print.core.aidl.AIDLBitmapPrinter
import com.quangph.jetpack.print.core.aidl.IAIDLPrinterService
import com.quangph.jetpack.print.core.bluetooth.BluetoothListByteArratPrinter
import com.quangph.jetpack.print.core.wifi.WifiListByteArrayPrinter
import com.quangph.jetpack.print.core.wifi.WifiPrinter

/**
 * Created by QuangPH on 2020-08-27.
 */
open class JetPrinterProxy(private val config: IJetPrinterConfig): IPrinter<Bitmap> {

    override fun print(data: Bitmap, numberOfCopy: Int): Boolean {
        if (config.getType() == JET_PRINTER_TYPE.AIDL_PRINTER) {
            return printAIDL(data, numberOfCopy)
        } else {
            return if (BluetoothAdapter.checkBluetoothAddress(config.getAddress())) {
                if (checkBluetoothIntegratedInside(config.getAddress())) {
                    printAIDL(data, numberOfCopy)
                } else {
                    printBluetooth(config.getAddress()!!, data, numberOfCopy, config.getType())
                }
            } else {
                if (config.getAddress() == null) {
                    return false
                } else {
                    printWifi(config.getAddress()!!, data, numberOfCopy, config.getType())
                }
            }
        }
    }

    open fun getAIDLService(): IAIDLPrinterService? {
        return null
    }

    /**
     * Some device has a Bluetooth Printer integrated inside, like Sunmi
     */
    open fun checkBluetoothIntegratedInside(address: String?): Boolean {
        return false
    }

    private fun printAIDL(data: Bitmap, numberOfCopy: Int): Boolean {
        val service = getAIDLService()
        return if (service != null) {
            val printer = AIDLBitmapPrinter(service)
            printer.print(data, numberOfCopy)
        } else {
            false
        }
    }

    private fun printBluetooth(address: String, data: Bitmap, numberOfCopy: Int, type: JET_PRINTER_TYPE): Boolean {
        if (type == JET_PRINTER_TYPE.ESC_PRINTER) {
            val dataAdapter = ESC_POS_PrinterDataAdapter(ESC_POS_Command())
            val printer = BluetoothPrinter(address)
            return printer.print(dataAdapter.generateData(data), numberOfCopy)
        } else if (type == JET_PRINTER_TYPE.TSC_PRINTER){
            val dataAdapter = TSC_PrinterDataAdapter()
            val printer = BluetoothListByteArratPrinter(address)
            return printer.print(dataAdapter.generateData(data), numberOfCopy)
        }
        return false
    }

    private fun printWifi(address: String, data: Bitmap, numberOfCopy: Int, type: JET_PRINTER_TYPE): Boolean {
        if (type == JET_PRINTER_TYPE.ESC_PRINTER) {
            val dataAdapter = ESC_POS_PrinterDataAdapter(ESC_POS_Command())
            val printer = WifiPrinter(address)
            return printer.print(dataAdapter.generateData(data), numberOfCopy)
        } else if (type == JET_PRINTER_TYPE.TSC_PRINTER){
            val dataAdapter = TSC_PrinterDataAdapter()
            val printer = WifiListByteArrayPrinter(address)
            return printer.print(dataAdapter.generateData(data), numberOfCopy)
        }
        return false
    }
}