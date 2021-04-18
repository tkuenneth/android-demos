package com.thomaskuenneth.asynctaskdemo

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task = object : AsyncTask<Int?, Int?, Int?>() {

            override fun doInBackground(vararg params: Int?): Int? {
                Log.d(TAG, "doInBackground entered")
                for (i in params) {
                    publishProgress(i)
                    Thread.sleep(10000)
                }
                return params.size
            }

            override fun onProgressUpdate(vararg values: Int?) {
                Log.d(TAG, "onProgressUpdate: ${values.first()}")
            }
        }
        task.execute(10, 3, 42, 5, 100)
    }
}