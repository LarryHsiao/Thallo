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
import android.os.Handler
import android.os.Looper
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Remote Device discovery by Bluetooth.
 *
 * Device UUID: e6c50c73-12ca-47d6-a718-e5ec97a6d407
 */
class BtDiscovery(private val context: Context, private val adapter: BluetoothAdapter) : Discovery {
    private val executor = Executors.newScheduledThreadPool(1)
    private val remoteDevice = ObservableArrayMap<String, CRemoteDevice>()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun start() {
        context.registerReceiver(receiver, IntentFilter().apply {
            addAction(ACTION_FOUND)
            addAction(ACTION_DISCOVERY_STARTED)
            addAction(ACTION_DISCOVERY_FINISHED)
            addAction(ACTION_UUID)
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
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE)
                    mainHandler.post {
                        device.fetchUuidsWithSdp()
                    }
                }
                ACTION_UUID -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE)
                    if (device.uuids != null) {
                        device.uuids.forEach {
                            if (it.uuid.toString() == "07d4a697-ece5-18a7-d647-ca12730cc5e6"){
                                remoteDevice[device.address] = BtRemoteDevice(device)
                            }
                        }
                    }
                }
            }
        }
    }
}