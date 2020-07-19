package com.thomaskuenneth.windowmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.window.ExtensionWindowBackend
import androidx.window.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backend = ExtensionWindowBackend.getInstance(this)
        val wm = WindowManager(this, backend)
    }
}