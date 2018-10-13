package com.silverhetch.thallo.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.*
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
        context.registerReceiver(receiver, IntentFilter().apply {
            addAction(ACTION_FOUND)
            addAction(ACTION_DISCOVERY_STARTED)
            addAction(ACTION_DISCOVERY_FINISHED)
        })
        executor.scheduleAtFixedRate({
            if (adapter.isDiscovering.not()) {
                // #startDiscovery() when previous not complete will cause #startDiscovery() blocks forever
                adapter.startDiscovery()
            }
        }, 0, 10, TimeUnit.SECONDS)
    }

    override fun stop() {
        context.unregisterReceiver(receiver)
        executor.shutdown()
    }

    override fun running(): Boolean {
        return adapter.isDiscovering
    }

    override fun remoteDevice(): ObservableMap<String, CRemoteDevice> {
        return remoteDevice
    }

    private val receiver = object : BroadcastReceiver() {
        private val tempDevices = ObservableArrayMap<String, CRemoteDevice>()
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE)

                    tempDevices[device.address] = BtRemoteDevice(device)
                }
                ACTION_DISCOVERY_FINISHED -> {
                    remoteDevice.clear()
                    remoteDevice.putAll(tempDevices.toMap())
                    tempDevices.clear()
                }
            }
        }
    }
}