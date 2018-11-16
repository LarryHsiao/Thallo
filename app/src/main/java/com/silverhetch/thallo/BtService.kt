package com.silverhetch.thallo

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.silverhetch.thallo.bluetooth.BtDiscovery
import com.silverhetch.thallo.bluetooth.Discovery
import java.util.*

/**
 * Bluetooth Service. All Bluetooth operation should remains here.
 */
class BtService : Service() {
    private lateinit var discovery: Discovery
    private val binder = BtServiceBinder()

    override fun onCreate() {
        super.onCreate()
        discovery = BtDiscovery(this, BluetoothAdapter.getDefaultAdapter(), UUID.fromString("07d4a697-ece5-18a7-d647-ca12730cc5e6"))
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