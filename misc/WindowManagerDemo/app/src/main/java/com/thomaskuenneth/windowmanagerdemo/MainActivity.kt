package com.thomaskuenneth.windowmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowMetricsCalculator
import com.thomaskuenneth.windowmanagerdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                output("computeCurrentWindowMetrics():")
                output(
                    "---> ${
                        WindowMetricsCalculator.getOrCreate()
                            .computeCurrentWindowMetrics(this@MainActivity).bounds
                    }"
                )
                output("\nwindowLayoutInfo:")
                WindowInfoTracker.getOrCreate(this@MainActivity)
                    .windowLayoutInfo(this@MainActivity).collect {
                        with(it.displayFeatures) {
                            if (isEmpty())
                                output("---> no features")
                            else
                                forEach { displayFeature ->
                                    (displayFeature as FoldingFeature).run {
                                        output("---> occlusionType: $occlusionType")
                                        output("---> orientation: $orientation")
                                        output("---> state: $state")
                                        output("---> isSeparating: $isSeparating")
                                        output("---> ${this.bounds}")
                                    }
                                }
                        }
                    }
            }
        }
    }

    private fun output(s: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding.textview) {
                if (length() > 0) append("\n")
                append(s)
            }
        }
    }
}
