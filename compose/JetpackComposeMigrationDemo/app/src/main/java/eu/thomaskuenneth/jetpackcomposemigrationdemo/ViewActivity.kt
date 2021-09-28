package eu.thomaskuenneth.jetpackcomposemigrationdemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.thomaskuenneth.jetpackcomposemigrationdemo.databinding.LayoutBinding
import eu.thomaskuenneth.jetpackcomposemigrationdemo.ui.theme.JetpackMigrationDemoTheme

class MyViewModel : ViewModel() {

    private val _sliderValue: MutableLiveData<Float> =
        MutableLiveData<Float>()

    val sliderValue: LiveData<Float>
        get() = _sliderValue

    fun setSliderValue(value: Float) {
        _sliderValue.value = value
    }
}

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: LayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MyViewModel by viewModels()
        viewModel.setSliderValue(0F)
        binding = LayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.slider.addOnChangeListener { _, value, _ -> viewModel.setSliderValue(value) }
        binding.composeView.setContent {
            val sliderValue = viewModel.sliderValue.observeAsState()
            sliderValue.value?.let {
                ComposeDemo(it)
            }
        }
    }
}

@Composable
fun ComposeDemo(value: Float) {
    JetpackMigrationDemoTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value.toString())
        }
    }
}