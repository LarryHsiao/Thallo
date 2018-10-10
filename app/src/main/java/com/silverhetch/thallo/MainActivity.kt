package com.silverhetch.thallo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.silverhetch.aura.AuraActivity

/**
 * Entry point of Thallo.
 */
class MainActivity : AuraActivity() {
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = FrameLayout(this)
        root.id = View.generateViewId()
        root.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setContentView(root)

        requestPermissionsByObj(arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    override fun onPermissionGranted() {
        super.onPermissionGranted()

        supportFragmentManager.beginTransaction().apply {
            replace(root.id, BtDiscoveryFragment())
            commit()
        }
    }
}
