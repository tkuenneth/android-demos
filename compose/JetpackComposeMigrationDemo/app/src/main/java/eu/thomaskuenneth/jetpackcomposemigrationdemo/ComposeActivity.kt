package eu.thomaskuenneth.jetpackcomposemigrationdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.thomaskuenneth.jetpackcomposemigrationdemo.ui.theme.JetpackMigrationDemoTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MyViewModel by viewModels()
        viewModel.setSliderValue(intent.getFloatExtra(KEY, 0F))
        setContent {
            ViewIntegrationDemo(viewModel.sliderValue.observeAsState(0F))
        }
    }
}

@Composable
fun ViewIntegrationDemo(sliderValueState: State<Float>) {
    JetpackMigrationDemoTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title =
                {
                    Text(text = stringResource(id = R.string.compose_activity))
                })
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {},
                    value = sliderValueState.value
                )
            }
        }
    }
}