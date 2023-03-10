package eu.thomaskuenneth.textviewhighlightsdemo

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Highlights
import androidx.appcompat.app.AppCompatActivity
import eu.thomaskuenneth.textviewhighlightsdemo.databinding.MainBinding

private const val TEXT = "*ABCD*"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val greenPaint = Paint().apply {
            color = Color.GREEN
        }
        val redPaint = Paint().apply {
            color = Color.RED
        }
        with(binding.textview1) {
            text = TEXT
            val builder = Highlights.Builder()
                .addRange(greenPaint, 1, 3)
                .addRange(redPaint, 3, 5)
            highlights = builder.build()
        }
        with(binding.textview2) {
            text = TEXT
            val builder = Highlights.Builder()
                .addRanges(redPaint, 0, 1, 3, 5)
            highlights = builder.build()
            // setSearchResultHighlights(1,3, 4, 6)
        }
    }
}