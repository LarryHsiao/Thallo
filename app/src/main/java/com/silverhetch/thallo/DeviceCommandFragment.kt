package com.silverhetch.thallo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silverhetch.thallo.discovery.CRemoteDevice
import kotlinx.android.synthetic.main.fragment_device_command.*
import java.util.*

/**
 * Send/receive data to remote devices.
 */
class DeviceCommandFragment : ThalloFragment() {
    companion object {
        private const val KEY_BLUETOOTH_ADDRESS = "KEY_BLUETOOTH_ADDRESS"
        fun newArguments(address: String): Bundle {
            return Bundle().apply {
                putString(KEY_BLUETOOTH_ADDRESS, address)

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_command, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceCommand_sendButton.setOnClickListener {
            deviceCommand_logView.update(Observable(), "Send data: ${deviceCommand_messageEditText.text}")
            deviceCommand_messageEditText.text.clear()
        }
    }

    override fun onBindService(devices: Map<String, CRemoteDevice>) {
        devices[arguments!!.getString(KEY_BLUETOOTH_ADDRESS)!!].let {device->
            if (device == null) {
                throw RuntimeException("Device not found: $KEY_BLUETOOTH_ADDRESS")
            }

            device.send("This is request")
        }
    }
}