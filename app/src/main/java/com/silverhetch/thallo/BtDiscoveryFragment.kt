package com.silverhetch.thallo

import android.app.Service.BIND_AUTO_CREATE
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.silverhetch.aura.AuraFragment
import com.silverhetch.thallo.discovery.CRemoteDevice
import kotlinx.android.synthetic.main.fragment_device_discovery.*

/**
 * Bluetooth device discovery list.
 */
class BtDiscoveryFragment : AuraFragment(), ServiceConnection {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_discovery, container, false)
    }

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
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        btDiscovery_list.adapter = ArrayAdapter<CRemoteDevice>(context!!, android.R.layout.simple_list_item_1).also {
            it.addAll((service as BtService.BtServiceBinder).discovery().remoteDevice())
        }
    }
}