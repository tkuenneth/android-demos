package com.thomaskuenneth.hyphenationdemo

import android.os.Bundle
import android.text.Layout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sb = StringBuilder()
        val textview = findViewById<TextView>(R.id.tv)
        when (textview.hyphenationFrequency) {
            Layout.HYPHENATION_FREQUENCY_NORMAL -> sb.append("HYPHENATION_FREQUENCY_NORMAL")
            Layout.HYPHENATION_FREQUENCY_FULL -> sb.append("HYPHENATION_FREQUENCY_FULL")
            Layout.HYPHENATION_FREQUENCY_NONE -> sb.append("HYPHENATION_FREQUENCY_NONE")
            else -> sb.append("unknwon")
        }
        sb.append("\n")
        sb.append("\n")
        for (i in 1..20) {
            if (sb.length > 0) {
                sb.append(" ")
            }
            sb.append("Die Datenschutzgrundverordnung.")
            sb.append(" ")
        }
        textview.text = sb.toString()
    }
}
