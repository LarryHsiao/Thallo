package com.silverhetch.thallo.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.util.*

/**
 * Device discovery by Bluetooth.
 */
class BtDiscovery(private val context: Context, private val adapter: BluetoothAdapter) : Discovery{
    private val remoteDevice = ArrayList<CRemoteDevice>()

    override fun start() {
        adapter.bondedDevices.forEach {
            remoteDevice.add(BtRemoteDevice(it))
        }
        context.registerReceiver(receiver, IntentFilter(ACTION_FOUND))
    }

    override fun stop() {
        context.unregisterReceiver(receiver)
        remoteDevice.clear()
    }

    override fun running(): Boolean {
        return adapter.isDiscovering
    }

    override fun remoteDevice(): List<CRemoteDevice> {
        return remoteDevice
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_FOUND == intent.action) {
                remoteDevice.add(BtRemoteDevice(intent.getParcelableExtra(EXTRA_DEVICE)))
            }
        }
    }
}