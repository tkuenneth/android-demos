package com.thomaskuenneth.toastorsnackbar

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var info: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info = findViewById(R.id.info)
    }

    fun handleSimpleClicked(view: View) {
        info.text = getString(R.string.simple)
        val simple = Toast.makeText(
            this,
            "Hello Toast",
            Toast.LENGTH_LONG
        )
        simple.setGravity(Gravity.CENTER, 0, 0)
        simple.show()
    }

    fun handleCustomClicked(view: View) {
        info.text = getString(R.string.custom)
        val image = ImageView(this)
        image.setImageDrawable(getDrawable(R.mipmap.ic_launcher_round))
        val custom = Toast(this)
        custom.duration = Toast.LENGTH_LONG
        custom.view = image
        custom.show()
    }

    fun handleSnackbarClicked(view: View) {
        info.text = getString(R.string.snackbar)
        val snackbar = Snackbar.make(info, info.text, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.dismiss) {
        }
        snackbar.show()
    }
}
