package com.silverhetch.thallo.discovery

/**
 * Device discovery.
 */
interface Discovery {

    /**
     * Stop discovery processes and release resources.
     */
    fun stop()

    /**
     * Start the enable.
     */
    fun start()

    /**
     * Indicate discovering process is running.
     */
    fun running(): Boolean

    /**
     * Current maintained remote devices.
     */
    fun remoteDevice(): List<CRemoteDevice>
}