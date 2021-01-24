package com.thomaskuenneth.threadingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.threadingdemo.databinding.ActivityDontDoItLikeThisBinding
import java.net.URL
import kotlin.concurrent.thread

class DontDoItLikeThis : AppCompatActivity() {

    private lateinit var binding: ActivityDontDoItLikeThisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDontDoItLikeThisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            thread {
                val msg = getHello()
                binding.textView.text = msg
            }
        }
    }
}

private fun getHello() = URL("http://10.0.2.2:8080/hello")
    .openStream()
    .bufferedReader()
    .use { it.readText() }
