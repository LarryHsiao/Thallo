package com.silverhetch.thallo.discovery

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.util.concurrent.ExecutorService

/**
 * Discovered device by Bluetooth.
 */
class BtRemoteDevice(private val bluetoothDevice: BluetoothDevice, private val executor: ExecutorService) : CRemoteDevice {
    override fun name(): String {
        return bluetoothDevice.name ?: ""
    }

    override fun toString(): String {
        return name() + " " + bluetoothDevice.address
    }

    override fun send(message: String) {
        executor.execute {
            val socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(Const.UUID_CARPO_DEVICE)
            socket.connect()

            PrintWriter(socket.outputStream).use {
                it.println("This is request")
            }

            val result = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length = socket.inputStream.read(buffer)
            while (length != -1) {
                result.write(buffer, 0, length)
                length = socket.inputStream.read(buffer)
            }
            Log.i("Bluetooth", "result: " + result.toString("UTF-8"))
        }
    }

    override fun address(): String {
        return bluetoothDevice.address
    }
}