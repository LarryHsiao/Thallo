package com.silverhetch.thallo

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.silverhetch.aura.AuraActivity

/**
 * Entry point of Thallo.
 */
class MainActivity : AuraActivity() {
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionsByObj(arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    override fun onPermissionGranted() {
        super.onPermissionGranted()
        val host = NavHostFragment.create(R.navigation.device_command)
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_navigation, host)
                .setPrimaryNavigationFragment(host)
                .commit()
    }
}
