package com.silverhetch.thallo

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.Observer
import com.silverhetch.aura.AuraFragment
import com.silverhetch.thallo.bluetooth.CRemoteDevice
import com.silverhetch.thallo.bluetooth.Discovery
import com.silverhetch.thallo.bluetooth.PhantomDiscovery

abstract class ThalloFragment : AuraFragment(), ServiceConnection {
    private val devices = HashMap<String, CRemoteDevice>()
    private lateinit var discovery: Discovery

    override fun onResume() {
        super.onResume()
        context!!.bindService(
                Intent(context, BtService::class.java),
                this,
                BIND_AUTO_CREATE
        )
    }

    override fun onPause() {
        super.onPause()
        context!!.unbindService(this)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        discovery = PhantomDiscovery()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        discovery = (service as BtService.BtServiceBinder).discovery()
        discovery.remoteDevice().observe(this, Observer<Map<String, CRemoteDevice>> {
            devices.putAll(it)
        })
        discovery.remoteDevice().value?.also {
            devices.putAll(it)
        }
        onBindService(devices)

    }
    abstract fun onBindService(devices: Map<String, CRemoteDevice>)
}