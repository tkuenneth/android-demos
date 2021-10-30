package eu.thomaskuenneth.hingedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import eu.thomaskuenneth.hingedemo.ui.theme.HingeDemoTheme

class HingeDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HingeDemoTheme {
                HingeDemo()
            }
        }
    }
}

@Composable
fun HingeDemo() {
    Scaffold {

    }
}