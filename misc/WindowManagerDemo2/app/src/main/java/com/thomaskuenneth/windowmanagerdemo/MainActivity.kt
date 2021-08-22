package com.thomaskuenneth.windowmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import com.thomaskuenneth.windowmanagerdemo.databinding.MainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repo = windowInfoRepository()
        lifecycleScope.launch {
            repo.currentWindowMetrics.collect { windowMetrics ->
                output("Bounds: ${windowMetrics.bounds.toShortString()}")
            }
        }
        lifecycleScope.launch {
            repo.windowLayoutInfo.collect {
                it.displayFeatures.forEach { displayFeature ->
                    (displayFeature as FoldingFeature).run {
                        output("occlusionType: $occlusionType")
                        output("orientation: $orientation")
                        output("state: $state")
                        output("isSeparating: $isSeparating")
                    }
                }
            }
        }
    }

    private fun output(s: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.textview.append("$s\n")
        }
    }
}