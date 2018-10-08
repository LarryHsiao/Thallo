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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = FrameLayout(this)
        root.id = View.generateViewId()
        root.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setContentView(root)

        supportFragmentManager.beginTransaction().apply {
            replace(root.id, BtDiscoveryFragment())
            commit()
        }
    }

}
