package com.silverhetch.thallo

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.silverhetch.thallo.discovery.BtDiscovery
import com.silverhetch.thallo.discovery.Discovery

/**
 * Bluetooth Service. All Bluetooth operation should remains here.
 */
class BtService : Service() {
    private lateinit var discovery: Discovery
    private val binder = BtServiceBinder()

    override fun onCreate() {
        super.onCreate()
        discovery = BtDiscovery(this, BluetoothAdapter.getDefaultAdapter())
        discovery.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        discovery.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class BtServiceBinder : Binder() {
        fun discovery(): Discovery {
            return discovery
        }
    }
}