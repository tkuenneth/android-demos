package com.thomaskuenneth.activitygroupdemo

import android.app.ActivityGroup
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ActivityGroup() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        first.addView(startActivity(R.string.first, Color.DKGRAY, Color.LTGRAY).decorView)
        second.addView(startActivity(R.string.second, Color.YELLOW, Color.BLUE).decorView)
    }

    private fun startActivity(text: Int, textColor: Int, layoutColor: Int): Window {
        val id1 = getString(text)
        val intent = Intent(this, ChildActivity::class.java)
        intent.putExtra(KEY_TEXT, id1)
        intent.putExtra(KEY_TEXT_COLOR, textColor)
        intent.putExtra(KEY_LAYOUT_COLOR, layoutColor)
        return localActivityManager.startActivity(id1, intent)
    }
}