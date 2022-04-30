package eu.thomaskuenneth.compose.marginpaddingdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import eu.thomaskuenneth.compose.marginpaddingdemo.ui.theme.MarginPaddingDemoTheme

class MarginPaddingDemoctivity : ComponentActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(this).inflate(R.layout.layout, null).let {
            setContent {
                MainScreen(it)
            }
        }
    }
}

@Composable
fun MainScreen(layout: View) {
    MarginPaddingDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    factory = {
                        layout
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {

                }
            }
        }
    }
}
