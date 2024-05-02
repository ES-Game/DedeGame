package com.quangph.jetpack.print.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import com.quangph.jetpack.print.core.IPrinter
import com.quangph.jetpack.print.core.stream.StreamingPrinter
import java.io.IOException
import java.util.*

/**
 * Created by QuangPH on 2020-08-26.
 */
class BluetoothPrinter(private val address: String): IPrinter<ByteArray> {

    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun print(data: ByteArray, numberOfCopy: Int): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
        var bluetoothSocket : BluetoothSocket? = null
        return try {
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
            bluetoothSocket.connect()
            if (bluetoothSocket.isConnected) {
                val printer = StreamingPrinter(bluetoothSocket.outputStream)
                return printer.print(data, numberOfCopy)
            } else {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        } finally {
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}