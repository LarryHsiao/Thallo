package com.silverhetch.thallo.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Device discovery by Bluetooth.
 */
class BtDiscovery(private val context: Context, private val adapter: BluetoothAdapter) : Discovery {
    private val executor = Executors.newScheduledThreadPool(1)
    private val remoteDevice = ObservableArrayMap<String, CRemoteDevice>()

    override fun start() {
        adapter.bondedDevices.forEach {
            remoteDevice[it.address] = BtRemoteDevice(it)
        }
        context.registerReceiver(receiver, IntentFilter(ACTION_FOUND))
        executor.scheduleAtFixedRate({
            Log.i("FixedRate", "Fixed rate")
            if (adapter.isDiscovering.not()) {
                remoteDevice.clear()
                adapter.startDiscovery()
            }
            Log.i("FixedRate", "Fixed rate2")
        }, 0, 10, TimeUnit.SECONDS)
    }

    override fun stop() {
        context.unregisterReceiver(receiver)
        remoteDevice.clear()
        executor.shutdown()
    }

    override fun running(): Boolean {
        return adapter.isDiscovering
    }

    override fun remoteDevice(): ObservableMap<String, CRemoteDevice> {
        return remoteDevice
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE)
                    remoteDevice[device.address] = BtRemoteDevice(device)
                }
            }
        }
    }
}