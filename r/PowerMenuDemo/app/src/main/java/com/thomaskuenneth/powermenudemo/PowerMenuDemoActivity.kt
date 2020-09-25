package com.thomaskuenneth.powermenudemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class PowerMenuDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent?.getStringExtra(KEY_MESSAGE)?.let {
            textview.text = it
        }
    }
}