package com.thomaskuenneth.customcomponentdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<EditableCheckbox>(R.id.my_editable_checkbox)
        view.checked = false
        view.text = "It just works"
    }
}