package com.silverhetch.thallo.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PhantomDiscovery : Discovery {
    override fun stop() {
    }

    override fun search() {
    }

    override fun start() {
    }

    override fun running(): Boolean {
        return false
    }

    override fun remoteDevice(): LiveData<MutableMap<String, CRemoteDevice>> {
        return MutableLiveData<MutableMap<String, CRemoteDevice>>()
    }
}