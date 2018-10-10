package com.silverhetch.thallo.discovery

import android.bluetooth.BluetoothDevice

/**
 * Discovered device by Bluetooth.
 */
class BtRemoteDevice(private val bluetoothDevice: BluetoothDevice) : CRemoteDevice {
    override fun name(): String {
        return bluetoothDevice.name ?:""
    }

    override fun toString(): String {
        return name() + " "+bluetoothDevice.address
    }
}