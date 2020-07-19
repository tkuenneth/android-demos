package com.thomaskuenneth.activitygroupdemo

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_child.*

const val KEY_TEXT = "text"
const val KEY_TEXT_COLOR = "text.color"
const val KEY_LAYOUT_COLOR = "layout.color"

class ChildActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)
        intent?.let {
            textview.text = it.getStringExtra(KEY_TEXT) ?: getString(R.string.app_name)
            textview.setTextColor(it.getIntExtra(KEY_TEXT_COLOR, Color.BLACK))
            layout.setBackgroundColor(it.getIntExtra(KEY_LAYOUT_COLOR, Color.WHITE))
            textview
        }
    }
}