package com.thomaskuenneth.threadingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.threadingdemo.databinding.ActivityDontDoItLikeThisBinding

class DontDoItLikeThis : AppCompatActivity() {

    private lateinit var binding: ActivityDontDoItLikeThisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDontDoItLikeThisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            binding.textView.text = getString(R.string.greeting)
        }
    }
}