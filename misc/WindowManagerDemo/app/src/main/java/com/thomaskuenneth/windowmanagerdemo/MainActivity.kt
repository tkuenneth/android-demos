package com.thomaskuenneth.windowmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.window.DeviceState
import androidx.window.ExtensionWindowBackend
import androidx.window.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val backend = ExtensionWindowBackend.getInstance(this)
        val wm = WindowManager(this, backend)
        window.decorView.doOnLayout {
            val posture = when (wm.deviceState.posture) {
                DeviceState.POSTURE_CLOSED -> "CLOSED"
                DeviceState.POSTURE_HALF_OPENED -> "HALF_OPENED"
                DeviceState.POSTURE_OPENED -> "OPENED"
                DeviceState.POSTURE_FLIPPED -> "FLIPPED"
                // DeviceState.POSTURE_UNKNOWN
                else -> "UNKNOWN"
            }
            textview.append("Posture: $posture\n")
            wm.windowLayoutInfo.displayFeatures.forEach {
                textview.append("$it.toString()\n")
            }
        }
    }
}