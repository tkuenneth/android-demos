package com.thomaskuenneth.windowmanagerdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.window.DeviceState
import androidx.window.ExtensionWindowBackend
import androidx.window.WindowManager

private val TAG = AppCompatActivity::class.simpleName

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val backend = ExtensionWindowBackend.getInstance(this)
        val wm = WindowManager(this, backend)

        val posture = when (wm.deviceState.posture) {
            DeviceState.POSTURE_CLOSED -> "CLOSED"
            DeviceState.POSTURE_HALF_OPENED -> "HALF_OPENED"
            DeviceState.POSTURE_OPENED -> "OPENED"
            DeviceState.POSTURE_FLIPPED -> "FLIPPED"
            // DeviceState.POSTURE_UNKNOWN
            else -> "UNKNOWN"
        }
        Log.d(TAG, "Posture: $posture")

        wm.windowLayoutInfo.displayFeatures.forEach {
            Log.d(TAG, it.toString())
        }
    }
}